package com.morgan.server.backend.prod;

import java.io.Serializable;

import javax.annotation.Nullable;

import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;

import com.google.inject.Inject;
import com.morgan.server.backend.AlarmBackend;
import com.morgan.server.backend.prod.alarmdb.AlarmDbHelper;

/**
 * Production implemenation of the {@link AlarmBackend} interface.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class ProdAlarmBackend implements AlarmBackend {

  private final AlarmDbHelper alarmHelper;

  @Inject ProdAlarmBackend(AlarmDbHelper alarmHelper) {
    this.alarmHelper = alarmHelper;
  }

  @Override public Iterable<PersistedAlarmDescription> readAllAlarms() {
    return alarmHelper.readAllAlarms();
  }

  @Override public long persistNewAlarm(
      ReadableInstant nextDeadline,
      @Nullable ReadableDuration repeatInterval,
      String alarmCallbackClass,
      @Nullable Serializable alarmData) {
    return alarmHelper.persistNewAlarm(
        nextDeadline,
        repeatInterval,
        alarmCallbackClass,
        alarmData);
  }

  @Override public void removeAlarm(long alarmId) {
    alarmHelper.removeAlarm(alarmId);
  }

  @Override public void updateAlarm(long alarmId, ReadableInstant nextDeadline) {
    alarmHelper.updateAlarm(alarmId, nextDeadline);
  }
}
