package com.morgan.server.util.stat;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.ReadableDuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.morgan.server.alarm.AlarmBuilder;
import com.morgan.server.alarm.AlarmCallback;
import com.morgan.server.alarm.AlarmManager;
import com.morgan.server.util.time.fake.FakeClock;

/**
 * Tests for the {@link DefaultStatisticsManager} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultStatisticsManagerTest {

  private static final ReadableDuration MAX_DURATION = Duration.standardDays(7L);

  @Mock private StatFlags mockFlags;
  @Mock private AlarmManager mockAlarmManager;
  @Mock private AlarmBuilder mockAlarmBuilder;

  @Captor private ArgumentCaptor<AlarmCallback> callbackCaptor;

  private FakeClock clock;
  private DefaultStatisticsManager manager;

  @Before public void createTestInstances() {
    when(mockAlarmManager.createTransientAlarm(any(AlarmCallback.class)))
        .thenReturn(mockAlarmBuilder);

    clock = new FakeClock(7L).setAutoIncrement(1000L);

    manager = new DefaultStatisticsManager(clock, mockFlags, mockAlarmManager);
  }

  @Before public void setUpCommonMockInteractions() {
    when(mockFlags.maximumHistoryDuration()).thenReturn(MAX_DURATION);
  }

  private AlarmCallback verifyAndReturnAlarmCallback() {
    InOrder order = inOrder(mockAlarmManager, mockAlarmBuilder);

    order.verify(mockAlarmManager).createTransientAlarm(callbackCaptor.capture());
    order.verify(mockAlarmBuilder).scheduleToRepeatEvery(DefaultStatisticsManager.ALARM_INTERVAL);

    return callbackCaptor.getValue();
  }

  @Test public void construction_registersAlarm() {
    verifyAndReturnAlarmCallback();
  }

  @Test public void addStatistic() {
    manager.openStatisticFor("one", "alpha").close();
    assertThat(manager.getStatistics()).containsExactly(new Statistic(
        "one", "alpha", new Instant(7L), new Instant(1007L), false));
  }

  @Test public void throwsOutOldInstances() {
    manager.openStatisticFor("one", "alpha").close();
    Instant newNow = new Instant(1007L).plus(MAX_DURATION);
    clock.setTime(newNow);
    manager.openStatisticFor("one", "alpha").close();
    manager.openStatisticFor("one", "alpha").close();
    verifyAndReturnAlarmCallback().handleAlarm(null, null);

    assertThat(manager.getStatistics()).containsExactly(
        new Statistic("one", "alpha", newNow, newNow.plus(1000L), false),
        new Statistic("one", "alpha", newNow.plus(2000L), newNow.plus(3000L), false));
  }
}
