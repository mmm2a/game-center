package com.morgan.server.alarm;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.truth.Truth;
import com.google.common.util.concurrent.ListenableScheduledFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.morgan.server.backend.AlarmBackend;
import com.morgan.server.backend.AlarmBackend.PersistedAlarmDescription;
import com.morgan.server.backend.DefaultPersistedAlarmDescription;
import com.morgan.server.util.fake.FakeExecutorService;
import com.morgan.server.util.time.fake.FakeClock;

/**
 * Tests for the {@link DefaultAlarmManager} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultAlarmManagerTest {

  private static final String DATA = "Hello data";

  @Mock private AlarmBackend mockBackend;
  @Mock private ListeningScheduledExecutorService mockScheduledExecutor;

  @Mock private ListenableScheduledFuture<Void> mockFuture;

  @Mock private AlarmCallback mockCallback;

  @Captor private ArgumentCaptor<Runnable> runnableCaptor;

  private FakeClock clock;
  private ListeningExecutorService backendExecutor;

  @Before public void createTestInstances() {
    backendExecutor = MoreExecutors.listeningDecorator(new FakeExecutorService());
    clock = new FakeClock();
  }

  @Before public void setUpCommonMockInteractions() {
    resetScheduledExecutor();
  }

  private void resetScheduledExecutor() {
    doReturn(mockFuture)
        .when(mockScheduledExecutor)
        .schedule(any(Runnable.class), anyLong(), any(TimeUnit.class));
  }

  private DefaultAlarmManager createManagerWithPersistedAlarms(
      PersistedAlarmDescription...persistedAlarms) {

    ImmutableList<PersistedAlarmDescription> alarms = ImmutableList.copyOf(persistedAlarms);
    when(mockBackend.readAllAlarms()).thenReturn(alarms);

    Injector inj = Guice.createInjector(new AbstractModule() {
      @Override protected void configure() {
        bind(AlarmCallback.class).toInstance(mockCallback);
      }
    });

    DefaultAlarmManager mgr = new DefaultAlarmManager(
            clock,
            inj,
            mockBackend,
            backendExecutor,
            mockScheduledExecutor);
    mgr.doStart();
    return mgr;
  }

  @Test public void scheduledTransientAlarm_doesntExecuteWhenNoAlarmsReady() {
    createManagerWithPersistedAlarms().createTransientAlarm(mockCallback)
        .setAlarmData(DATA)
        .scheduleFor(new Instant(1000));
    verify(mockScheduledExecutor)
        .schedule(runnableCaptor.capture(), eq(1000L), eq(TimeUnit.MILLISECONDS));
    runnableCaptor.getValue().run();
    verifyZeroInteractions(mockCallback);
  }

  @Test public void scheduledTransientAlarm_enforcesMinimumSleepTime() {
    createManagerWithPersistedAlarms().createTransientAlarm(mockCallback)
        .setAlarmData(DATA)
        .scheduleFor(new Instant(10));
    verify(mockScheduledExecutor)
        .schedule(runnableCaptor.capture(), eq(100L), eq(TimeUnit.MILLISECONDS));
    runnableCaptor.getValue().run();
    verifyZeroInteractions(mockCallback);
  }

  @Test public void scheduledTransientAlarm_executesIfTimeHasOccurred() {
    AlarmHandle handle = createManagerWithPersistedAlarms().createTransientAlarm(mockCallback)
        .setAlarmData(DATA)
        .scheduleFor(new Instant(1000));
    verify(mockScheduledExecutor)
        .schedule(runnableCaptor.capture(), eq(1000L), eq(TimeUnit.MILLISECONDS));
    clock.setTime(2000L);
    runnableCaptor.getValue().run();
    verify(mockCallback).handleAlarm(handle, Optional.<Object>of(DATA));
  }

  @Test public void scheduledTransientAlarm_executesIfTimeHasOccurred_onlyExecutesOnce() {
    AlarmHandle handle = createManagerWithPersistedAlarms().createTransientAlarm(mockCallback)
        .setAlarmData(DATA)
        .scheduleFor(new Instant(1000));
    verify(mockScheduledExecutor)
        .schedule(runnableCaptor.capture(), eq(1000L), eq(TimeUnit.MILLISECONDS));
    clock.setTime(2000L);
    runnableCaptor.getValue().run();
    verify(mockCallback).handleAlarm(handle, Optional.<Object>of(DATA));
    runnableCaptor.getValue().run();
    verifyNoMoreInteractions(mockCallback);
  }

  @Test public void scheduledTransientAlarm_multipleAlarms_executeWhenReady() {
    String data1 = "data 1";
    String data2 = "data 2";
    String data3 = "data 3";

    DefaultAlarmManager manager = createManagerWithPersistedAlarms();

    AlarmHandle handle1 = manager.createTransientAlarm(mockCallback)
        .setAlarmData(data1)
        .scheduleFor(new Instant(1000));
    AlarmHandle handle2 = manager.createTransientAlarm(mockCallback)
        .setAlarmData(data2)
        .scheduleFor(new Instant(1050));

    reset(mockScheduledExecutor);
    resetScheduledExecutor();

    AlarmHandle handle3 = manager.createTransientAlarm(mockCallback)
        .setAlarmData(data3)
        .scheduleFor(new Instant(3000));
    verify(mockScheduledExecutor)
        .schedule(runnableCaptor.capture(), eq(1000L), eq(TimeUnit.MILLISECONDS));
    reset(mockScheduledExecutor);
    resetScheduledExecutor();

    clock.setTime(2000L);
    runnableCaptor.getValue().run();
    verify(mockCallback).handleAlarm(handle1, Optional.<Object>of(data1));
    verify(mockCallback).handleAlarm(handle2, Optional.<Object>of(data2));
    verify(mockCallback, never()).handleAlarm(handle3, Optional.<Object>of(data3));

    verify(mockScheduledExecutor)
        .schedule(runnableCaptor.capture(), eq(1000L), eq(TimeUnit.MILLISECONDS));
    clock.setTime(3001L);
    runnableCaptor.getValue().run();
    verify(mockCallback).handleAlarm(handle3, Optional.<Object>of(data3));
    verifyNoMoreInteractions(mockCallback);
  }

  @Test public void scheduledTransientAlarm_cancel_cancelsAlarm() {
    AlarmHandle handle = createManagerWithPersistedAlarms().createTransientAlarm(mockCallback)
        .setAlarmData(DATA)
        .scheduleFor(new Instant(1000));
    verify(mockScheduledExecutor)
        .schedule(runnableCaptor.capture(), eq(1000L), eq(TimeUnit.MILLISECONDS));
    clock.setTime(2000L);
    handle.cancel();
    runnableCaptor.getValue().run();
    verifyZeroInteractions(mockCallback);
  }

  @Test public void scheduledRepeatingTransientAlarm_repeats() {
    AlarmHandle handle = createManagerWithPersistedAlarms().createTransientAlarm(mockCallback)
        .setAlarmData(DATA)
        .scheduleToRepeatEvery(Duration.standardSeconds(5));
    verify(mockScheduledExecutor)
        .schedule(runnableCaptor.capture(), eq(5000L), eq(TimeUnit.MILLISECONDS));
    reset(mockScheduledExecutor);
    resetScheduledExecutor();
    clock.setTime(6000L);
    runnableCaptor.getValue().run();
    verify(mockCallback).handleAlarm(handle, Optional.<Object>of(DATA));
    reset(mockCallback);
    verify(mockScheduledExecutor)
        .schedule(runnableCaptor.capture(), eq(5000L), eq(TimeUnit.MILLISECONDS));
    clock.setTime(12000L);
    runnableCaptor.getValue().run();
    verify(mockCallback).handleAlarm(handle, Optional.<Object>of(DATA));
  }

  @Test public void previouslyScheduledPersistentAlarm_reloads() {
    String data1 = "data 1";
    String data2 = "data 2";
    String data3 = "data 3";

    DefaultPersistedAlarmDescription d1 = new DefaultPersistedAlarmDescription(
        1L, new Instant(5000L), null, AlarmCallback.class.getName(), data1);
    DefaultPersistedAlarmDescription d2 = new DefaultPersistedAlarmDescription(
        2L, new Instant(6000L), Duration.standardSeconds(5),
        AlarmCallback.class.getName(), data2);
    DefaultPersistedAlarmDescription d3 = new DefaultPersistedAlarmDescription(
        3L, new Instant(7000L), null, AlarmCallback.class.getName(), data3);
    createManagerWithPersistedAlarms(d1, d2, d3);
    verify(mockScheduledExecutor)
        .schedule(runnableCaptor.capture(), eq(5000L), eq(TimeUnit.MILLISECONDS));

    reset(mockScheduledExecutor);
    resetScheduledExecutor();

    clock.setTime(8000L);
    runnableCaptor.getValue().run();

    verify(mockCallback).handleAlarm(any(AlarmHandle.class), eq(Optional.<Object>of(data1)));
    verify(mockCallback).handleAlarm(any(AlarmHandle.class), eq(Optional.<Object>of(data2)));
    verify(mockCallback).handleAlarm(any(AlarmHandle.class), eq(Optional.<Object>of(data3)));
  }

  @Test public void schedulePersistedAlarm_persistsInDatabase() {
    String data = "Data was here";
    when(mockBackend.persistNewAlarm(new Instant(7000L), null, AlarmCallback.class.getName(), data))
        .thenReturn(69L);

    createManagerWithPersistedAlarms().createPersistentAlarm(AlarmCallback.class)
        .setAlarmData(data)
        .scheduleFor(new Instant(7000L));
    verify(mockBackend)
        .persistNewAlarm(new Instant(7000L), null, AlarmCallback.class.getName(), data);
  }

  @Test public void schedulePersistedAlarm_executedTaskIsRemovedFromDb() {
    String data = "Data was here";
    when(mockBackend.persistNewAlarm(new Instant(7000L), null, AlarmCallback.class.getName(), data))
        .thenReturn(69L);

    createManagerWithPersistedAlarms().createPersistentAlarm(AlarmCallback.class)
        .setAlarmData(data)
        .scheduleFor(new Instant(7000L));
    verify(mockScheduledExecutor)
        .schedule(runnableCaptor.capture(), eq(7000L), eq(TimeUnit.MILLISECONDS));
    clock.setTime(8000L);
    runnableCaptor.getValue().run();
    verify(mockCallback).handleAlarm(any(AlarmHandle.class), eq(Optional.<Object>of(data)));
    verify(mockBackend).removeAlarm(69L);
  }

  @Test public void schedulePersistedAlarm_repeating_updatesDbAfterExecute() {
    String data = "Data was here";
    when(mockBackend.persistNewAlarm(
        new Instant(7000L), Duration.standardSeconds(7L), AlarmCallback.class.getName(), data))
        .thenReturn(69L);

    createManagerWithPersistedAlarms().createPersistentAlarm(AlarmCallback.class)
        .setAlarmData(data)
        .scheduleToRepeatEvery(Duration.standardSeconds(7L));
    verify(mockScheduledExecutor)
        .schedule(runnableCaptor.capture(), eq(7000L), eq(TimeUnit.MILLISECONDS));
    clock.setTime(8000L);
    runnableCaptor.getValue().run();
    verify(mockBackend).updateAlarm(69L, new Instant(15000L));
  }

  @Test public void schedulePersistedAlarm_cancelled_removesFromDatabase() {
    String data = "Data was here";
    when(mockBackend.persistNewAlarm(new Instant(7000L), null, AlarmCallback.class.getName(), data))
        .thenReturn(69L);

    AlarmHandle handle = createManagerWithPersistedAlarms()
        .createPersistentAlarm(AlarmCallback.class)
        .setAlarmData(data)
        .scheduleFor(new Instant(7000L));
    handle.cancel();
    verify(mockBackend).removeAlarm(69L);
  }

  @Test public void schedulePersistedAlarm_alarmIdIsPersistent() {
    String data = "Data was here";
    when(mockBackend.persistNewAlarm(new Instant(7000L), null, AlarmCallback.class.getName(), data))
        .thenReturn(69L);

    AlarmHandle handle = createManagerWithPersistedAlarms()
        .createPersistentAlarm(AlarmCallback.class)
        .setAlarmData(data)
        .scheduleFor(new Instant(7000L));
    Truth.assertThat(handle.getAlarmId().isPersistent()).isTrue();
    Truth.assertThat(((PersistentAlarmId) handle.getAlarmId()).getKey()).isEqualTo(69L);
  }

  @Test public void lookupAlarm() {
    String data = "Data was here";
    when(mockBackend.persistNewAlarm(new Instant(7000L), null, AlarmCallback.class.getName(), data))
        .thenReturn(69L);

    DefaultAlarmManager manager = createManagerWithPersistedAlarms();
    AlarmHandle handle = manager.createPersistentAlarm(AlarmCallback.class)
        .setAlarmData(data)
        .scheduleFor(new Instant(7000L));
    AlarmId id = handle.getAlarmId();
    Truth.assertThat(manager.lookupAlarm(id)).isEqualTo(handle);
  }
}
