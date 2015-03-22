package com.morgan.server.util.stat;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.morgan.server.util.stat.StatisticsSync.StatHandle;

/**
 * Tests for the {@link StatisticsMeasurer} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class StatisticsMeasurerTest {

  private static final String RETURN_VALUE = "Hello, World!";
  private static final Class<?> CLASS = String.class;

  @Mock private StatisticsSync mockSync;
  @Mock private StatHandle mockHandle;
  @Mock private MethodInvocation mockInv;

  private StatisticsMeasurer measurer;
  private Method method;

  @Before public void createTestInstances() throws Throwable {
    measurer = new StatisticsMeasurer(mockSync);
    method = CLASS.getMethod("hashCode");

    when(mockInv.getMethod()).thenReturn(method);
    when(mockSync.openStatisticFor(method)).thenReturn(mockHandle);
    when(mockInv.proceed()).thenReturn(RETURN_VALUE);
  }

  @Test public void invoke_returnsResult() throws Throwable {
    assertThat(measurer.invoke(mockInv)).isEqualTo(RETURN_VALUE);

    InOrder order = inOrder(mockInv, mockSync, mockHandle);
    order.verify(mockSync).openStatisticFor(method);
    order.verify(mockInv).proceed();
    order.verify(mockHandle).close();
  }

  @Test public void invoke_throwsException() throws Throwable {
    Throwable cause = new NullPointerException();
    when(mockInv.proceed()).thenThrow(cause);

    try {
      measurer.invoke(mockInv);
      Assert.fail("Expected exception didn't occur");
    } catch (NullPointerException e) {
      // Expected exception
      assertThat(e).isEqualTo(cause);
    }

    InOrder order = inOrder(mockInv, mockSync, mockHandle);
    order.verify(mockSync).openStatisticFor(method);
    order.verify(mockInv).proceed();
    order.verify(mockHandle).markFailedWith(cause);
    order.verify(mockHandle).close();
  }
}
