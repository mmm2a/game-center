package com.morgan.server.backend.fake;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morgan.server.backend.AlarmBackend;
import com.morgan.server.backend.DefaultPersistedAlarmDescription;

/**
 * Fake implementation of the {@link AlarmBackend}.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
class FakeAlarmBackend implements AlarmBackend {

  private final Map<Long, PersistedAlarmDescription> descriptions = new HashMap<>();

  private long nextId = 0L;

  @Inject FakeAlarmBackend() {
  }

  @Override public Iterable<PersistedAlarmDescription> readAllAlarms() {
    synchronized(descriptions) {
      return ImmutableList.copyOf(descriptions.values());
    }
  }

  @Override public long persistNewAlarm(
      ReadableInstant nextDeadline,
      @Nullable ReadableDuration repeatInterval,
      String alarmCallbackClass,
      @Nullable Serializable alarmData) {
    synchronized(descriptions) {
      DefaultPersistedAlarmDescription desc = new DefaultPersistedAlarmDescription(
          nextId++,
          nextDeadline,
          repeatInterval,
          alarmCallbackClass,
          alarmData);
      descriptions.put(desc.getId(), desc);
      return desc.getId();
    }
  }

  @Override public void removeAlarm(long alarmId) {
    synchronized(descriptions) {
      descriptions.remove(alarmId);
    }
  }

  @Override public void updateAlarm(long alarmId, ReadableInstant nextDeadline) {
    synchronized(descriptions) {
      PersistedAlarmDescription old = descriptions.remove(alarmId);
      if (old != null) {
        DefaultPersistedAlarmDescription desc = new DefaultPersistedAlarmDescription(
            old.getId(),
            nextDeadline,
            old.getRepeatInterval(),
            old.getAlarmCallbackClass(),
            old.getAlarmData());
        descriptions.put(desc.getId(), desc);
      }
    }
  }
}
