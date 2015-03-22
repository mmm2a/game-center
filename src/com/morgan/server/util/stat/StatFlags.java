package com.morgan.server.util.stat;

import org.joda.time.ReadableDuration;

import com.morgan.server.util.flag.Flag;
import com.morgan.server.util.flag.FlagAccessor;
import com.morgan.server.util.time.DurationFlagParser;

/**
 * Flags interface for configuring the statistics package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface StatFlags extends FlagAccessor {

  @Flag(name = "max-stat-history-duration",
      description = "Maximum duration of time that the server will keep a statistic around",
      required = false,
      defaultValue = "7d",
      parser = DurationFlagParser.class)
  ReadableDuration maximumHistoryDuration();
}
