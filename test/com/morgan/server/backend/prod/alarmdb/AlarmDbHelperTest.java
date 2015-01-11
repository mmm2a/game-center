package com.morgan.server.backend.prod.alarmdb;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.google.inject.util.Providers;
import com.morgan.server.backend.DefaultPersistedAlarmDescription;

/**
 * Tests for the {@link AlarmDbHelper} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class AlarmDbHelperTest {

  @Mock private EntityManager mockEntityManager;

  @Mock private TypedQuery<AlarmEntity> mockQuery;

  private AlarmDbHelper helper;

  @Before public void createTestInstances() {
    helper = new AlarmDbHelper(Providers.of(mockEntityManager));
  }

  @Before public void setUpCommonMockInteractions() {
    when(mockEntityManager.createNamedQuery(anyString(), eq(AlarmEntity.class)))
        .thenReturn(mockQuery);
  }

  @Test public void readAllAlarms() {
    long id1 = 1000L;
    long id2 = 2000L;
    long id3 = 3000L;

    ReadableInstant deadline1 = new Instant(101L);
    ReadableInstant deadline2 = new Instant(102L);
    ReadableInstant deadline3 = new Instant(103L);

    Date date1 = deadline1.toInstant().toDate();
    Date date2 = deadline2.toInstant().toDate();
    Date date3 = deadline3.toInstant().toDate();

    long durationMs1 = 5000L;

    ReadableDuration duration1 = Duration.standardSeconds(durationMs1 / 1000);
    ReadableDuration duration2 = null;
    ReadableDuration duration3 = null;

    String callbackClass1 = "callback-class-1";
    String callbackClass2 = "callback-class-2";
    String callbackClass3 = "callback-class-3";

    String data1 = "data1";
    String data2 = "data2";
    String data3 = null;

    AlarmEntity entity1 = new AlarmEntity(date1, callbackClass1, durationMs1, data1);
    AlarmEntity entity2 = new AlarmEntity(date2, callbackClass2, null, data2);
    AlarmEntity entity3 = new AlarmEntity(date3, callbackClass3, null, data3);

    entity1.id = id1;
    entity2.id = id2;
    entity3.id = id3;

    when(mockQuery.getResultList()).thenReturn(ImmutableList.of(
        entity1, entity2, entity3));

    assertThat(helper.readAllAlarms()).iteratesAs(
        new DefaultPersistedAlarmDescription(id1, deadline1, duration1, callbackClass1, data1),
        new DefaultPersistedAlarmDescription(id2, deadline2, duration2, callbackClass2, data2),
        new DefaultPersistedAlarmDescription(id3, deadline3, duration3, callbackClass3, data3));

    verify(mockEntityManager).createNamedQuery("findAllAlarms", AlarmEntity.class);
  }

  @Test public void persistNewAlarm() {
    Instant deadline = new Instant(1000L);
    ReadableDuration duration = Duration.standardSeconds(5L);
    String callbackClass = "callback-class";
    String data = "alarm data";

    helper.persistNewAlarm(deadline, duration, callbackClass, data);
    verify(mockEntityManager).persist(new AlarmEntity(new Date(1000L), callbackClass, 5000L, data));
  }

  @Test public void removeAlarm() {
    AlarmEntity entity = new AlarmEntity(new Date(1000L), "callback-class", 42L, "data");
    entity.id = 69L;

    when(mockEntityManager.find(AlarmEntity.class, 69L)).thenReturn(entity);
    helper.removeAlarm(69L);
    verify(mockEntityManager).remove(entity);
  }

  @Test public void updateAlarm() {
    AlarmEntity entity = new AlarmEntity(new Date(1000L), "callback-class", 42L, "data");
    entity.id = 69L;

    when(mockEntityManager.find(AlarmEntity.class, 69L)).thenReturn(entity);
    helper.updateAlarm(69L, new Instant(755L));
    assertThat(entity.getNextOccurrence()).isEqualTo(new Date(755L));
  }
}
