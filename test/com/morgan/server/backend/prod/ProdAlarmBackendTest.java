package com.morgan.server.backend.prod;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.morgan.server.backend.AlarmBackend.PersistedAlarmDescription;
import com.morgan.server.backend.prod.alarmdb.AlarmDbHelper;

/**
 * Tests for the {@link ProdAlarmBackend} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class ProdAlarmBackendTest {

  @Mock private AlarmDbHelper mockHelper;

  @Mock private PersistedAlarmDescription mockPad1;
  @Mock private PersistedAlarmDescription mockPad2;
  @Mock private PersistedAlarmDescription mockPad3;

  private ProdAlarmBackend backend;

  @Before public void createTestInstances() {
    backend = new ProdAlarmBackend(mockHelper);
  }

  @Test public void readAllAlarms() {
    when(mockHelper.readAllAlarms()).thenReturn(ImmutableList.of(mockPad1, mockPad2, mockPad3));
    assertThat(backend.readAllAlarms()).iteratesAs(mockPad1, mockPad2, mockPad3);
  }

  @Test public void persistNewAlarm_nullDurationAndData_noExceptions() {
    ReadableInstant deadline = new Instant(7L);
    String callbackClass = "callbackClass";

    when(mockHelper.persistNewAlarm(deadline, null, callbackClass, null)).thenReturn(42L);
    assertThat(backend.persistNewAlarm(deadline, null, callbackClass, null)).isEqualTo(42L);
  }

  @Test public void persistNewAlarm() {
    ReadableInstant deadline = new Instant(7L);
    ReadableDuration duration = Duration.standardSeconds(69L);
    String callbackClass = "callbackClass";
    String data = "foo bar";

    when(mockHelper.persistNewAlarm(deadline, duration, callbackClass, data)).thenReturn(42L);
    assertThat(backend.persistNewAlarm(deadline, duration, callbackClass, data)).isEqualTo(42L);
  }

  @Test public void removeAlarm() {
    backend.removeAlarm(7L);
    verify(mockHelper).removeAlarm(7L);
  }

  @Test public void updateAlarm() {
    ReadableInstant newDeadline = new Instant(1001L);
    backend.updateAlarm(7L, newDeadline);
    verify(mockHelper).updateAlarm(7L, newDeadline);
  }
}
