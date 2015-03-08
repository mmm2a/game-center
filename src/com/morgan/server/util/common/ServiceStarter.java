package com.morgan.server.util.common;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.Service;
import com.google.inject.Inject;
import com.morgan.server.util.log.AdvancedLogger;
import com.morgan.server.util.log.InjectLogger;

/**
 * Utility class for starting up all services that have been configured.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class ServiceStarter {

  private final ImmutableSet<Service> services;

  @InjectLogger private AdvancedLogger log = AdvancedLogger.NULL;

  @Inject ServiceStarter(Set<Service> services) {
    this.services = ImmutableSet.copyOf(services);
  }

  /**
   * Requests that all services that have been registered be started.
   */
  public void startServices() {
    int count = 0;
    log.info("Starting all services");
    for (Service service : services) {
      log.info("[Service #%d] Starting service %s", ++count, service);
      service.startAsync();
    }
    log.info("Finished starting %d services", count);
  }
}
