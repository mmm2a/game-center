package com.morgan.server.alarm;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.morgan.server.backend.AlarmBackend;
import com.morgan.server.backend.AlarmBackend.PersistedAlarmDescription;
import com.morgan.server.common.CommonBindingAnnotations.Background;
import com.morgan.server.util.log.AdvancedLogger;
import com.morgan.server.util.time.Clock;

/**
 * Default implementation of the {@link AlarmManager} interface.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class DefaultAlarmManager implements AlarmManager, Runnable {

  private static final AdvancedLogger log = new AdvancedLogger(DefaultAlarmManager.class);

  private static final int INITIAL_CAPACITY = 32;
  private static final long MINIMUM_SLEEP_TIME_MS = 100L;

  private static final Comparator<AlarmOccurrence> alarmOccurrenceComparator =
      new Comparator<DefaultAlarmManager.AlarmOccurrence>() {
        @Override public int compare(AlarmOccurrence o1, AlarmOccurrence o2) {
          return o1.getDeadline().compareTo(o2.getDeadline());
        }
      };

  private final Clock clock;
  private final AlarmBackend alarmBackend;
  private final Injector injector;
  private final ExecutorService executorService;
  private final ScheduledExecutorService scheduledExecutorService;

  private final PriorityQueue<AlarmOccurrence> occurrences =
      new PriorityQueue<>(INITIAL_CAPACITY, alarmOccurrenceComparator);
  private final Map<AlarmId, AlarmDescription> alarmDescriptions = new HashMap<>();

  @Nullable private ScheduledFuture<?> nextOccurrenceFuture;

  @Inject DefaultAlarmManager(
      Clock clock,
      Injector injector,
      AlarmBackend alarmBackend,
      @Background ExecutorService executorService,
      ScheduledExecutorService scheduledExecutorService) {
    this.clock = clock;
    this.injector = injector;
    this.alarmBackend = alarmBackend;
    this.executorService = executorService;
    this.scheduledExecutorService = scheduledExecutorService;

    for (PersistedAlarmDescription pad : alarmBackend.readAllAlarms()) {
      AlarmId id = new PersistentAlarmId(pad.getId());
      AlarmOccurrence occurrence = new AlarmOccurrence(id, pad.getNextDeadline());

      try {
        PersistentAlarmDescription description = new PersistentAlarmDescription(
            pad.getId(),
            findCallbackClass(pad.getAlarmCallbackClass()),
            Optional.<Object>fromNullable(pad.getAlarmData()),
            pad.getRepeatInterval());

        // No need to synchronize because this is being done in the constructor
        occurrences.add(occurrence);
        alarmDescriptions.put(id, description);
      } catch (Throwable cause) {
        log.warning(cause, "Failed to load persisted alarm %d", pad.getId());
      }
    }

    scheduleNextAlarmCycle();
  }

  private AlarmHandle scheduleNewAlarm(
      ReadableInstant nextOccurrence, AlarmDescription alarmDescription) {
    synchronized (occurrences) {
      synchronized (alarmDescriptions) {
        AlarmOccurrence occurrence = new AlarmOccurrence(
            alarmDescription.getAlarmId(), nextOccurrence);
        occurrences.add(occurrence);
        alarmDescriptions.put(alarmDescription.getAlarmId(), alarmDescription);

        scheduleNextAlarmCycle();
      }
    }

    return alarmDescription;
  }

  private AlarmHandle createTransientAlarm(
      AlarmCallback callback,
      Optional<Object> data,
      ReadableInstant deadline,
      @Nullable ReadableDuration repeatDuration) {
    TransientAlarmDescription description =
        new TransientAlarmDescription(callback, data, repeatDuration);
    return scheduleNewAlarm(deadline, description);
  }

  private AlarmHandle createPersistentAlarm(
      Class<? extends AlarmCallback> callbackClass,
      Optional<Object> data,
      ReadableInstant deadline,
      @Nullable ReadableDuration repeatDuration) {
    long alarmId = alarmBackend.persistNewAlarm(
        deadline, repeatDuration, callbackClass.getName(), (Serializable) data.orNull());
    PersistentAlarmDescription description = new PersistentAlarmDescription(
        alarmId, callbackClass, data, repeatDuration);
    return scheduleNewAlarm(deadline, description);
  }

  private void scheduleNextAlarmCycle() {
    synchronized(occurrences) {
      if (nextOccurrenceFuture != null) {
        nextOccurrenceFuture.cancel(false);
        nextOccurrenceFuture = null;
      }

      AlarmOccurrence nextOccurrence = occurrences.peek();
      if (nextOccurrence != null) {
        long interval = Math.max(MINIMUM_SLEEP_TIME_MS,
            nextOccurrence.deadline.getMillis() - clock.now().getMillis());
        nextOccurrenceFuture = scheduledExecutorService.schedule(
            this, interval, TimeUnit.MILLISECONDS);
      }
    }
  }

  private Class<? extends AlarmCallback> findCallbackClass(String className)
      throws ClassNotFoundException {
    Class<?> cl = Thread.currentThread().getContextClassLoader().loadClass(className);
    return cl.asSubclass(AlarmCallback.class);
  }

  private void cancelAlarm(AlarmId alarmId) {
    // We don't worry about removing the alarm from the occurences queue; when that fires, it'll
    // fail to find the alarm and just give up.
    synchronized(alarmDescriptions) {
      alarmDescriptions.remove(alarmId);
    }
  }

  @Override
  @Nullable
  public AlarmHandle lookupAlarm(AlarmId alarmId) {
    synchronized(alarmDescriptions) {
      return alarmDescriptions.get(alarmId);
    }
  }

  @Override public AlarmBuilder createTransientAlarm(AlarmCallback callback) {
    return new AlarmBuilderImpl(callback);
  }

  @Override public AlarmBuilder createPersistentAlarm(Class<? extends AlarmCallback> callbackClass) {
    return new AlarmBuilderImpl(callbackClass);
  }

  @Override public void run() {
    synchronized(occurrences) {
      synchronized(alarmDescriptions) {
        nextOccurrenceFuture = null;
        ReadableInstant now = clock.now();
        while (!occurrences.isEmpty() && occurrences.peek().deadline.isBefore(now)) {
          AlarmOccurrence occurrence = occurrences.remove();
          AlarmDescription description = alarmDescriptions.get(occurrence.getId());
          if (description != null) {
            ReadableInstant nextDeadline = description.fireAlarm();
            if (nextDeadline != null) {
              AlarmOccurrence nextOccurrence =
                  new AlarmOccurrence(occurrence.getId(), nextDeadline);
              occurrences.add(nextOccurrence);
            }
          }
        }
      }
    }

    scheduleNextAlarmCycle();
  }

  /**
   * Description of a specific occurrence of an alarm that is scheduled.
   */
  private static class AlarmOccurrence {

    private final AlarmId alarmId;
    private final ReadableInstant deadline;

    AlarmOccurrence(AlarmId alarmId, ReadableInstant deadline) {
      this.alarmId = Preconditions.checkNotNull(alarmId);
      this.deadline = deadline;
    }

    AlarmId getId() {
      return alarmId;
    }

    ReadableInstant getDeadline() {
      return deadline;
    }
  }

  /**
   * Interface for a description that is stored internally and which can fire the alarm represented.
   */
  private interface AlarmDescription extends AlarmHandle {
    /**
     * Ask this description to fire the represented alarm.  Returns {@code null} if the alarm should
     * not fire again, otherwise the next deadline to fire it at.
     */
    @Nullable ReadableInstant fireAlarm();
  }

  /**
   * Internal representation of a persistent alarm.
   */
  private class PersistentAlarmDescription implements Runnable, AlarmHandle, AlarmDescription {

    private final long alarmId;
    private final Class<? extends AlarmCallback> callbackClass;
    private final Optional<Object> data;
    @Nullable private final ReadableDuration repeatDuration;

    PersistentAlarmDescription(
        long alarmId,
        Class<? extends AlarmCallback> callbackClass,
        Optional<Object> data,
        @Nullable ReadableDuration repeatDuration) {
      this.alarmId = alarmId;
      this.callbackClass = Preconditions.checkNotNull(callbackClass);
      this.data = Preconditions.checkNotNull(data);
      this.repeatDuration = repeatDuration;
    }

    @Override public void run() {
      try {
        AlarmCallback callback = injector.getInstance(callbackClass);
        callback.handleAlarm(this, data);
      } catch (Exception cause) {
        log.warning(cause,
            "Error trying to call persisted alarm %d with callback %s", alarmId, callbackClass);
      }
    }

    @Override public AlarmId getAlarmId() {
      return new PersistentAlarmId(alarmId);
    }

    @Override public void cancel() {
      cancelAlarm(getAlarmId());
      alarmBackend.removeAlarm(alarmId);
    }

    @Nullable
    @Override
    public ReadableInstant fireAlarm() {
      executorService.execute(this);

      ReadableInstant nextDeadline = null;
      if (repeatDuration != null) {
        nextDeadline = clock.now().toInstant().plus(repeatDuration);
        alarmBackend.updateAlarm(alarmId, nextDeadline);
      } else {
        alarmBackend.removeAlarm(alarmId);
      }

      return nextDeadline;
    }
  }

  /**
   * Description of a transient alarm being managed by this alarm manager.
   */
  private class TransientAlarmDescription
      implements Runnable, AlarmHandle, AlarmDescription, AlarmId {

    private final AlarmCallback callback;
    private final Optional<Object> data;
    @Nullable private final ReadableDuration repeatDuration;

    TransientAlarmDescription(
        AlarmCallback callback,
        Optional<Object> data,
        @Nullable ReadableDuration repeatDuration) {
      this.callback = Preconditions.checkNotNull(callback);
      this.data = Preconditions.checkNotNull(data);
      this.repeatDuration = repeatDuration;
    }

    @Nullable
    @Override
    public ReadableInstant fireAlarm() {
      executorService.execute(this);

      ReadableInstant nextDeadline = null;
      if (repeatDuration != null) {
        nextDeadline = clock.now().toInstant().plus(repeatDuration);
      }
      return nextDeadline;
    }

    @Override public void run() {
      callback.handleAlarm(this, data);
    }

    @Override public AlarmId getAlarmId() {
      return this;
    }

    @Override public void cancel() {
      cancelAlarm(this);
    }

    @Override public boolean isPersistent() {
      return false;
    }
  }

  /**
   * Internal implementation of an {@link AlarmBuilder}.
   */
  private class AlarmBuilderImpl implements AlarmBuilder {

    @Nullable private final AlarmCallback transientCallback;
    @Nullable private final Class<? extends AlarmCallback> persistentCallbackClass;

    @Nullable private Object data;

    AlarmBuilderImpl(AlarmCallback transientCallback) {
      this.transientCallback = Preconditions.checkNotNull(transientCallback);
      this.persistentCallbackClass = null;
    }

    AlarmBuilderImpl(Class<? extends AlarmCallback> persistentCallbackClass) {
      this.transientCallback = null;
      this.persistentCallbackClass = Preconditions.checkNotNull(persistentCallbackClass);
    }

    @Override public AlarmBuilder setAlarmData(@Nullable Object data) {
      if (persistentCallbackClass != null) {
        Preconditions.checkArgument(data == null || data instanceof Serializable,
            "Persisted data must implement Serializable");
      }

      this.data = data;
      return this;
    }

    @Override public AlarmHandle scheduleFor(ReadableInstant time) {
      Preconditions.checkNotNull(time);
      if (transientCallback != null) {
        return createTransientAlarm(transientCallback, Optional.fromNullable(data), time, null);
      } else {
        return createPersistentAlarm(
            persistentCallbackClass, Optional.fromNullable(data), time, null);
      }
    }

    @Override public AlarmHandle scheduleIn(ReadableDuration duration) {
      Preconditions.checkNotNull(duration);
      return scheduleFor(clock.now().toInstant().plus(duration));
    }

    @Override public AlarmHandle scheduleToRepeatEvery(ReadableDuration duration) {
      Preconditions.checkNotNull(duration);

      ReadableInstant time = clock.now().toInstant().plus(duration);
      if (transientCallback != null) {
        return createTransientAlarm(transientCallback, Optional.fromNullable(data), time, duration);
      } else {
        return createPersistentAlarm(
            persistentCallbackClass, Optional.fromNullable(data), time, duration);
      }
    }
  }
}
