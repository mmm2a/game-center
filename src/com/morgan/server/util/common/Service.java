package com.morgan.server.util.common;

/**
 * An interface for a type that wants to be started as a service AFTER eager singletons are loaded.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface Service {

  /**
   * Indicates the priority with which this service should be started.  Lower number services are
   * started first, then higher ones.  Services that advertise the same priority can be started
   * in any order.
   */
  int getServicePriority();

  /**
   * Attempts to start this service.
   */
  void start();
}
