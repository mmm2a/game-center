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

  @Mock private Server mockServer;
  @Mock private WebAppContext mockWebAppContext;

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
  }

  @Test(expected = IllegalStateException.class) public void start_noConnectors() throws Exception {
    server.start();
  }

  private void verifyCommonServerStartBehavior() throws Exception {
    verify(mockWebAppContext).setResourceBase("./war");
    verify(mockWebAppContext).setDescriptor("./war/WEB-INF/web.xml");
    verify(mockWebAppContext).setContextPath("/");
    verify(mockWebAppContext).setParentLoaderPriority(true);
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
    verifyCommonServerStartBehavior();
  }

  @Test public void start_onlyOneConnector() throws Exception {
    when(mockConnectorFactory2.createServerConnector(mockServer))
        .thenReturn(Optional.of(mockConnector2));

    server.start();

    verify(mockServer, never()).addConnector(mockConnector1);
    verify(mockServer).addConnector(mockConnector2);
    verifyCommonServerStartBehavior();
  }

  private class TestableGameServer extends GameServer {

    TestableGameServer() {
      super(
          Providers.of(mockServer),
          Providers.of(mockWebAppContext),
          ImmutableSet.of(mockConnectorFactory1, mockConnectorFactory2));
    }

    @Override void startAndJoin(Server server) {
      Preconditions.checkArgument(server == mockServer);
    }
  }
}
