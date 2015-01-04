package com.morgan.server.backend.prod;

import java.io.Serializable;

import javax.annotation.Nullable;

import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;

import com.google.inject.Inject;
import com.morgan.server.backend.AlarmBackend;

/**
 * Production implemenation of the {@link AlarmBackend} interface.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class ProdAlarmBackend implements AlarmBackend {

  @Inject ProdAlarmBackend() {
  }

  @Override public Iterable<PersistedAlarmDescription> readAllAlarms() {
    throw new UnsupportedOperationException();
  }

  @Override public long persistNewAlarm(
      ReadableInstant nextDeadline,
      @Nullable ReadableDuration repeatInterval,
      String alarmCallbackClass,
      @Nullable Serializable alarmData) {
    throw new UnsupportedOperationException();
  }

  @Override public void removeAlarm(long alarmId) {
    throw new UnsupportedOperationException();
  }

  @Override public void updateAlarm(long alarmId, ReadableInstant nextDeadline) {
    throw new UnsupportedOperationException();
  }
}
