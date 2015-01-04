package com.morgan.server.alarm;

/**
 * Handle to a registered alarm.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface AlarmHandle {

  /** Retrieves a unique ID that can be used to look up the alarm later. */
  AlarmId getAlarmId();

  /** Cancels this alarm. */
  void cancel();
}
