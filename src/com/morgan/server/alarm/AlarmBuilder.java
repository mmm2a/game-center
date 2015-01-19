package com.morgan.server.alarm;

import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;

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