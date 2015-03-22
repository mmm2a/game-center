package com.morgan.server.util.stat;

import java.util.Objects;

import org.joda.time.Duration;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Data class that stores information about a collected statistic.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
final class Statistic {

  private final String context;
  private final String action;

  private final ReadableInstant startTime;
  private final ReadableInstant stopTime;
  private final ReadableDuration duration;
  private final boolean wasFailed;

  Statistic(
      String context, String action,
      ReadableInstant startTime, ReadableInstant stopTime, boolean wasFailed) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(context));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(action));

    this.context = context;
    this.action = action;

    this.startTime = Preconditions.checkNotNull(startTime);
    this.stopTime = Preconditions.checkNotNull(stopTime);

    Preconditions.checkArgument(!startTime.isAfter(stopTime));
    this.duration = Duration.millis(stopTime.getMillis() - startTime.getMillis());
    this.wasFailed = wasFailed;
  }

  String getContext() {
    return context;
  }

  String getAction() {
    return action;
  }

  ReadableInstant getStartTime() {
    return startTime;
  }

  ReadableInstant getStopTime() {
    return stopTime;
  }

  ReadableDuration getDuration() {
    return duration;
  }

  boolean wasFailed() {
    return wasFailed;
  }

  @Override public int hashCode() {
    return Objects.hash(context, action, startTime, stopTime, duration, wasFailed);
  }

  @Override public boolean equals(Object o) {
    if (!(o instanceof Statistic)) {
      return false;
    }

    Statistic other = (Statistic) o;
    return context.equals(other.context)
        && action.equals(other.action)
        && startTime.equals(other.startTime)
        && stopTime.equals(other.stopTime)
        && duration.equals(other.duration)
        && wasFailed == other.wasFailed;
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(Statistic.class)
        .add("context", context)
        .add("action", action)
        .add("startTime", startTime)
        .add("stopTime", stopTime)
        .add("duration", duration)
        .add("wasFailed", wasFailed)
        .toString();
  }
}
