package com.morgan.server.alarm;

import javax.annotation.Nullable;

import com.google.inject.ImplementedBy;

/**
 * A manager that manages alarms in the server.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@ImplementedBy(DefaultAlarmManager.class)
public interface AlarmManager extends TransientAlarmManager {

  /**
   * Looks up an alarm by its ID.  If the alarm can't be found, this method returns {@code null}.
   */
  @Nullable AlarmHandle lookupAlarm(AlarmId alarmId);

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
