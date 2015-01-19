package com.morgan.client.common;

import com.google.common.base.Preconditions;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;

/**
 * Helper class that makes it easier to use the {@link Timer} class with GWT and much easier to
 * debug with it.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class TimerFactory {

  /**
   * An interface for a handle to a timer.
   */
  public interface TimerHandle {
    /** Cancels this timer */
    void cancel();
  }

  @Inject TimerFactory() {
  }

  /**
   * Schedules a {@link Runnable} to run in <i>delayMillis</i> milliseconds.
   */
  public TimerHandle schedule(final Runnable runnable, int delayMillis) {
    Preconditions.checkNotNull(runnable);
    Preconditions.checkArgument(delayMillis >= 0);

    final Timer timer = new Timer() {
      @Override public void run() {
        runnable.run();
      }
    };
    timer.schedule(delayMillis);
    return new TimerHandle() {
      @Override public void cancel() {
        timer.cancel();
      }
    };
  }

  /**
   * Schedules a {@link Runnable} to run every <i>periodMillis</i> milliseconds.
   */
  public TimerHandle scheduleRepeating(final Runnable runnable, int periodMillis) {
    Preconditions.checkNotNull(runnable);
    Preconditions.checkArgument(periodMillis >= 0);

    final Timer timer = new Timer() {
      @Override public void run() {
        runnable.run();
      }
    };
    timer.scheduleRepeating(periodMillis);
    return new TimerHandle() {
      @Override public void cancel() {
        timer.cancel();
      }
    };
  }
}
