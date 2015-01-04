package com.morgan.server.alarm;

import com.google.common.base.Optional;

/**
 * An interface for a callback that will receive a call when an alarm goes off.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface AlarmCallback {
  /**
   * Callback method invoked when an alarm fires off.
   *
   * @param alarm a handle to the alarm that was fired.
   * @param alarmData an optional data object that can be registered when the alarm is created.
   */
  void handleAlarm(AlarmHandle alarm, Optional<Object> alarmData);
}
