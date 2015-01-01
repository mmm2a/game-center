package com.morgan.server.util.time;

import org.joda.time.ReadableInstant;

import com.google.inject.ImplementedBy;

/**
 * Interface for a type that can get the time of day.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@ImplementedBy(DefaultClock.class)
public interface Clock {

  /**
   * Gets the time of day for right now.
   */
  ReadableInstant now();
}
