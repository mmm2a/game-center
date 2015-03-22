package com.morgan.server.util.stat;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.morgan.server.util.stat.StatisticsSync.StatHandle;

/**
 * AOP {@link MethodInterceptor} to measure statistics on methods annotated with the
 * {@link MeasureStatistics} annotation.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class StatisticsMeasurer implements MethodInterceptor {

  @Inject private StatisticsSync statSync;

  @VisibleForTesting StatisticsMeasurer(StatisticsSync statSync) {
    this.statSync = statSync;
  }

  StatisticsMeasurer() {
  }

  @Override public Object invoke(MethodInvocation inv) throws Throwable {
    StatHandle handle = statSync.openStatisticFor(inv.getMethod());
    try {
      return inv.proceed();
    } catch (Throwable e) {
      handle.markFailedWith(e);
      throw e;
    } finally {
      handle.close();
    }
  }
}
