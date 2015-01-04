package com.morgan.server.backend;

import java.io.Serializable;

import javax.annotation.Nullable;

import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;

/**
 * Backend for manipulating persistent alarms.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface AlarmBackend {
  /**
   * Interface for communicating back to a caller a description of a persisted alarm.
   *
   * @author mark@mark-morgan.net (Mark Morgan)
   */
  public interface PersistedAlarmDescription {
    long getId();
    ReadableInstant getNextDeadline();
    @Nullable ReadableDuration getRepeatInterval();
    String getAlarmCallbackClass();
    @Nullable Serializable getAlarmData();
  }

  /**
   * Reads all alarms that have been persisted.
   */
  Iterable<PersistedAlarmDescription> readAllAlarms();

  /**
   * Persists a new alarm into the database. Returns the unique ID for the alarm.
   */
  long persistNewAlarm(
      ReadableInstant nextDeadline,
      @Nullable ReadableDuration repeatInterval,
      String alarmCallbackClass,
      @Nullable Serializable alarmData);

  /**
   * Removes an alarm completely from the database.
   */
  void removeAlarm(long alarmId);

  /**
   * Updates an existing alarm to change its deadline.
   */
  void updateAlarm(long alarmId, ReadableInstant nextDeadline);
}
