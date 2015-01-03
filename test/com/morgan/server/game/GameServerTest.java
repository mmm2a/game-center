package com.morgan.server.game;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.inject.util.Providers;

/**
 * Tests for the {@link GameServer} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class GameServerTest {

  private static final String WAR = "war";
  private static final String WAR_CONTEXT_PATH = "context-path";
  private static final String WAR_RESOURCE_BASE = "resource-base";
  private static final String WAR_DESCRIPTOR = "descriptor";
  private static final boolean WAR_PARENT_LOADER_PRIORITY = true;

  @Mock private Server mockServer;
  @Mock private WebAppContext mockWebAppContext;

  @Mock private GameServerFlagAccessor mockFlagAccessor;
  @Mock private ServerConnectorFactory mockConnectorFactory1;
  @Mock private ServerConnectorFactory mockConnectorFactory2;

  @Mock private ServerConnector mockConnector1;
  @Mock private ServerConnector mockConnector2;

  @Mock private ServletContextHandler mockContextHandler;

  private TestableGameServer server;

  @Before public void createTestInstances() {
    server = new TestableGameServer();
  }

  @Before public void setUpCommonMockInteractions() {
    when(mockConnectorFactory1.createServerConnector(mockServer))
        .thenReturn(Optional.<ServerConnector>absent());
    when(mockConnectorFactory2.createServerConnector(mockServer))
        .thenReturn(Optional.<ServerConnector>absent());

    when(mockFlagAccessor.warFile()).thenReturn(WAR);
    when(mockFlagAccessor.warContextPath()).thenReturn(WAR_CONTEXT_PATH);
    when(mockFlagAccessor.warResourceBase()).thenReturn(WAR_RESOURCE_BASE);
    when(mockFlagAccessor.warDescriptorPath()).thenReturn(WAR_DESCRIPTOR);
    when(mockFlagAccessor.isParentLoaderPriority()).thenReturn(WAR_PARENT_LOADER_PRIORITY);
  }

  @Test(expected = IllegalStateException.class) public void start_noConnectors() throws Exception {
    server.start();
  }

  private void verifyCommonServerStartBehavior(boolean isWar) throws Exception {
    verify(mockWebAppContext).setContextPath(WAR_CONTEXT_PATH);

    if (isWar) {
      verify(mockWebAppContext).setWar(WAR);
    } else {
      verify(mockWebAppContext).setResourceBase(WAR_RESOURCE_BASE);
      verify(mockWebAppContext).setDescriptor(WAR_DESCRIPTOR);
      verify(mockWebAppContext).setParentLoaderPriority(WAR_PARENT_LOADER_PRIORITY);
    }

    verify(mockServer).setHandler(mockWebAppContext);
  }

  @Test public void start_bothConnectors() throws Exception {
    when(mockConnectorFactory1.createServerConnector(mockServer))
        .thenReturn(Optional.of(mockConnector1));
    when(mockConnectorFactory2.createServerConnector(mockServer))
        .thenReturn(Optional.of(mockConnector2));

    server.start();

    verify(mockServer).addConnector(mockConnector1);
    verify(mockServer).addConnector(mockConnector2);
    verifyCommonServerStartBehavior(true);
  }

  @Test public void start_onlyOneConnector() throws Exception {
    when(mockConnectorFactory2.createServerConnector(mockServer))
        .thenReturn(Optional.of(mockConnector2));

    server.start();

    verify(mockServer, never()).addConnector(mockConnector1);
    verify(mockServer).addConnector(mockConnector2);
    verifyCommonServerStartBehavior(true);
  }

  @Test public void start_notWarFile() throws Exception {
    when(mockConnectorFactory1.createServerConnector(mockServer))
        .thenReturn(Optional.of(mockConnector1));
    when(mockFlagAccessor.warFile()).thenReturn(null);
    server.start();
    verifyCommonServerStartBehavior(false);
  }

  private class TestableGameServer extends GameServer {

    TestableGameServer() {
      super(
          mockFlagAccessor,
          Providers.of(mockServer),
          Providers.of(mockWebAppContext),
          ImmutableSet.of(mockConnectorFactory1, mockConnectorFactory2));
    }

    @Override void startAndJoin(Server server) {
      Preconditions.checkArgument(server == mockServer);
    }
  }
}
