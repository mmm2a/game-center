package com.morgan.server.alarm;

/**
 * Interface for an alarm manager that only deals with transient alarms.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface TransientAlarmManager {

  /**
   * Creates an alarm that only exists in memory.  If the server exits and restarts, the alarm is
   * lost.
   */
  AlarmBuilder createTransientAlarm(AlarmCallback callback);
}
