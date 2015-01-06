package com.morgan.server.alarm;

import javax.annotation.Nullable;

import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;

import com.google.inject.ImplementedBy;

/**
 * A manager that manages alarms in the server.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@ImplementedBy(DefaultAlarmManager.class)
public interface AlarmManager {

  /**
   * Interface for a builder that builds new alarms.
   */
  public interface AlarmBuilder {
    /**
     * Sets persistent data that will be echoed back to the registered {@link AlarmCallback} when
     * the alarm files.  Setting this data is <b>not</b> required.  If this alarm is persistent,
     * then the data set for the alarm MUST implement {@link java.io.Serializable}.
     */
    AlarmBuilder setAlarmData(Object data);

    /**
     * Schedules this alarm to occur at a specific time in the future.  If the time given is not
     * in the future, then the alarm fires as soon as possible.
     */
    AlarmHandle scheduleFor(ReadableInstant time);

    /**
     * Schedules this alarm to occur in a certain duration of time.
     */
    AlarmHandle scheduleIn(ReadableDuration duration);

    /**
     * Schedules this alarm to occur at a regular, repeating interval.
     */
    AlarmHandle scheduleToRepeatEvery(ReadableDuration duration);
  }

  /**
   * Looks up an alarm by its ID.  If the alarm can't be found, this method returns {@code null}.
   */
  @Nullable AlarmHandle lookupAlarm(AlarmId alarmId);

  /**
   * Creates an alarm that only exists in memory.  If the server exits and restarts, the alarm is
   * lost.
   */
  AlarmBuilder createTransientAlarm(AlarmCallback callback);

  /**
   * Creates an alarm that is persisted into the database to ensure that it isn't lost on a server
   * restart.  If the server restarts, and the alarm should have fired when the server is down, then
   * the alarm will fire as soon as possible after server restart.
   *
   * <p>The callback class MUST be GUICE instantiable.  Its up to the caller to guarantee that
   * this instantiation has the proper semantics (singleton, not singleton, etc.).
   */
  AlarmBuilder createPersistentAlarm(Class<? extends AlarmCallback> callbackClass);
}
