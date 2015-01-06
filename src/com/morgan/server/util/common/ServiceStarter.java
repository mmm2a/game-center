package com.morgan.server.util.common;

import java.util.Comparator;
import java.util.Set;

import com.google.common.collect.ImmutableSortedSet;
import com.google.inject.Inject;
import com.morgan.server.util.log.AdvancedLogger;

/**
 * Utility class for starting up all services that have been configured.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class ServiceStarter {

  private static final AdvancedLogger log = new AdvancedLogger(ServiceStarter.class);

  private static final Comparator<Service> SERVICE_COMPARATOR =
      new Comparator<Service>() {
        @Override public int compare(Service o1, Service o2) {
          return o1.getServicePriority() - o2.getServicePriority();
        }
      };

  private final ImmutableSortedSet<Service> services;

  @Inject ServiceStarter(Set<Service> services) {
    this.services = ImmutableSortedSet.copyOf(SERVICE_COMPARATOR, services);
  }

  /**
   * Requests that all services that have been registered be started.
   */
  public void startServices() {
    int count = 0;
    log.info("Starting all services");
    for (Service service : services) {
      log.info("[Service #%d] Starting service %s at priority %d",
          ++count, service, service.getServicePriority());
      service.start();
    }
    log.info("Finished starting %d services", count);
  }
}
