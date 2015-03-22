package com.morgan.server.util.stat;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;

import org.joda.time.Duration;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morgan.server.alarm.AlarmCallback;
import com.morgan.server.alarm.AlarmHandle;
import com.morgan.server.alarm.AlarmManager;
import com.morgan.server.util.time.Clock;

/**
 * Default implementation of a store that can note statistics and report them back.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
class DefaultStatisticsManager implements StatisticsSync {

  @VisibleForTesting static final ReadableDuration ALARM_INTERVAL = Duration.standardMinutes(1);

  private final AlarmCallback alarmCallback = new AlarmCallback() {
    @Override public void handleAlarm(AlarmHandle alarm, Optional<Object> alarmData) {
      handleCleanupAlarm();
    }
  };

  private final LinkedList<Statistic> statistics = new LinkedList<>();

  private final Clock clock;
  private final StatFlags statFlags;

  @Inject DefaultStatisticsManager(
      Clock clock,
      StatFlags statFlags,
      AlarmManager alarmManager) {
    this.clock = clock;
    this.statFlags = statFlags;

    alarmManager.createTransientAlarm(alarmCallback)
        .scheduleToRepeatEvery(ALARM_INTERVAL);
  }

  @VisibleForTesting ImmutableList<Statistic> getStatistics() {
    synchronized(statistics) {
      return ImmutableList.copyOf(statistics);
    }
  }

  private void handleCleanupAlarm() {
    ReadableInstant throwOutTime = clock.now()
        .toInstant()
        .minus(statFlags.maximumHistoryDuration());

    synchronized(statistics) {
      Iterator<Statistic> iter = statistics.iterator();
      while (iter.hasNext()) {
        Statistic stat = iter.next();
        if (throwOutTime.isBefore(stat.getStartTime())) {
          // We know the list is in order (or, close enough), so we can stop iterating once we fail
          // to find an entry old enough to throw out.
          break;
        }
        iter.remove();
      }
    }
  }

  private void addStatistic(Statistic statistic) {
    synchronized(statistics) {
      statistics.add(statistic);
    }
  }

  @Override public StatHandle openStatisticFor(Method action) {
    return openStatisticFor(action.getDeclaringClass().getName(), action.getName());
  }

  @Override public StatHandle openStatisticFor(String contextName, String actionName) {
    return new InternalStatHandle(contextName, actionName);
  }

  /**
   * Internal implementation of a {@link StatHandle}.
   */
  private class InternalStatHandle implements StatHandle {

    private final String contextName;
    private final String actionName;

    private final ReadableInstant startTime;

    private Optional<Throwable> failureCause = Optional.absent();

    InternalStatHandle(String contextName, String actionName) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(contextName));
      Preconditions.checkArgument(!Strings.isNullOrEmpty(actionName));

      this.startTime = clock.now();
      this.contextName = contextName;
      this.actionName = actionName;
    }

    @Override public void close() {
      ReadableInstant stopTime = clock.now();
      Statistic stat = new Statistic(
          contextName, actionName, startTime, stopTime, failureCause.isPresent());
      addStatistic(stat);
    }

    @Override public void markFailedWith(Throwable cause) {
      this.failureCause = Optional.of(cause);
    }
  }
}
