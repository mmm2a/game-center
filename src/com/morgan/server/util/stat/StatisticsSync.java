package com.morgan.server.util.stat;

import java.lang.reflect.Method;

/**
 * Type that can receive statistics information.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface StatisticsSync {

  /**
   * Handle to an ongoing statistics measurement.
   */
  public interface StatHandle extends AutoCloseable {
    /**
     * Mark's this particular stat handle as a failed call with a given cause.
     */
    void markFailedWith(Throwable cause);

    @Override void close();
  }

  /**
   * Opens a new handle and starts measurement of a statistic for a method in a class.
   */
  StatHandle openStatisticFor(Method action);

  /**
   * Opens a new handle and starts measurement of a statistic for a method in a class.
   */
  StatHandle openStatisticFor(String contextName, String methodName);
}
