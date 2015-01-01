package com.morgan.server.util.time;

import org.joda.time.Instant;
import org.joda.time.ReadableInstant;

/**
 * Default implementation of the {@link Clock} interface.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
final class DefaultClock implements Clock {

  @Override public ReadableInstant now() {
    return new Instant();
  }
}
