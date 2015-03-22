package com.morgan.server.util.stat;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.morgan.server.util.flag.FlagAccessorFactory;

/**
 * Guice module for the statistics package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class StatModule extends AbstractModule {

  @Override protected void configure() {
    bind(StatisticsSync.class).to(DefaultStatisticsManager.class);

    StatisticsMeasurer measurer = new StatisticsMeasurer();
    requestInjection(measurer);
    bindInterceptor(Matchers.any(), Matchers.annotatedWith(MeasureStatistics.class), measurer);
  }

  @Provides @Singleton protected StatFlags provideStatFlagsAccessor(
      FlagAccessorFactory accessorFactory) {
    return accessorFactory.getFlagAccessor(StatFlags.class);
  }
}
