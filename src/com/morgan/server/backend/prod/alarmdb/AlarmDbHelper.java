package com.morgan.server.backend.prod.alarmdb;

import java.io.Serializable;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.morgan.server.alarm.PersistentAlarmId;
import com.morgan.server.backend.AlarmBackend.PersistedAlarmDescription;
import com.morgan.server.backend.DefaultPersistedAlarmDescription;

/**
 * A helper class for helping with database access related to alarms.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AlarmDbHelper {

  private final Provider<EntityManager> entityManagerProvider;

  @Inject AlarmDbHelper(Provider<EntityManager> entityManagerProvider) {
    this.entityManagerProvider = entityManagerProvider;
  }

  @Transactional
  public Iterable<PersistedAlarmDescription> readAllAlarms() {
    EntityManager entityManager = entityManagerProvider.get();
    return Iterables.transform(
        entityManager.createNamedQuery("findAllAlarms", AlarmEntity.class).getResultList(),
        new DbToServerFunc(entityManager));
  }

  @Transactional
  public long persistNewAlarm(
      ReadableInstant nextDeadline,
      @Nullable ReadableDuration repeatInterval,
      String alarmCallbackClass,
      @Nullable Serializable alarmData) {
    AlarmEntity entity = new AlarmEntity(
        nextDeadline.toInstant().toDate(),
        alarmCallbackClass,
        (repeatInterval == null) ? null : repeatInterval.getMillis(),
        alarmData);
    entityManagerProvider.get().persist(entity);
    return entity.getId();
  }

  @Transactional
  public void removeAlarm(long alarmId) {
    EntityManager entityManager = entityManagerProvider.get();
    entityManager.remove(entityManager.find(AlarmEntity.class, alarmId));
  }

  @Transactional
  public void updateAlarm(long alarmId, ReadableInstant nextDeadline) {
    EntityManager entityManager = entityManagerProvider.get();
    AlarmEntity entity = entityManager.find(AlarmEntity.class, alarmId);
    entity.setNextOccurrence(nextDeadline.toInstant().toDate());
  }

  /**
   * Internal function to translate {@link AlarmEntity} instances into
   * {@link PersistedAlarmDescription} instances.
   */
  private static class DbToServerFunc implements Function<AlarmEntity, PersistedAlarmDescription> {

    private final EntityManager entityManager;

    DbToServerFunc(EntityManager entityManager) {
      this.entityManager = entityManager;
    }

    @Override @Nullable public PersistedAlarmDescription apply(@Nullable AlarmEntity input) {
      if (input == null) {
        return null;
      }

      // Might as well detach it
      entityManager.detach(input);

      Long repeatInterval = input.getRepeatInterval();
      return new DefaultPersistedAlarmDescription(
          input.getId(),
          new Instant(input.getNextOccurrence()),
          (repeatInterval == null) ? (ReadableDuration) null : new Duration(repeatInterval),
          input.getAlarmCallbackClass(),
          input.getAlarmData());
    }
  };
}
