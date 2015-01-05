package com.morgan.server.util.time.fake;

import org.joda.time.Instant;
import org.joda.time.ReadableInstant;

import com.google.common.base.Preconditions;
import com.morgan.server.util.time.Clock;

/**
 * Fake {@link Clock} implementation for testing purposes.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class FakeClock implements Clock {

  private long time;
  private long autoIncrement;
  
  public FakeClock(long time) {
    setTime(time);
  }
  
  public FakeClock() {
    this(0L);
  }
  
  public FakeClock setTime(long time) {
    Preconditions.checkArgument(time >= 0);
    
    this.time = time;
    return this;
  }
  
  public FakeClock setAutoIncrement(long autoIncrement) {
    Preconditions.checkArgument(autoIncrement >= 0);
    return this;
  }
  
  @Override public ReadableInstant now() {
    ReadableInstant ret = new Instant(time);
    time += autoIncrement;
    return ret;
  }
}
