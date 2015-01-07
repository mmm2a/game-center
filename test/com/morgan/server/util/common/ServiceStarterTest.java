package com.morgan.server.util.common;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.Service;

/**
 * Tests for the {@link ServiceStarter} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class ServiceStarterTest {

  @Mock private Service mockService1;
  @Mock private Service mockService2;
  @Mock private Service mockService3;

  private ServiceStarter starter;

  @Before public void createTestInstances() {
    starter = new ServiceStarter(ImmutableSet.of(mockService1, mockService2, mockService3));
  }

  @Test public void startServices_startsAllRegisteredServices() {
    starter.startServices();

    verify(mockService1).startAsync();
    verify(mockService2).startAsync();
    verify(mockService3).startAsync();
  }
}
