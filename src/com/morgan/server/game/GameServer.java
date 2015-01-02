package com.morgan.server.game;

import java.util.EnumSet;
import java.util.Set;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceFilter;

/**
 * Main server class for the game engine.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
class GameServer {

  private final Provider<Server> serverProvider;
  private final ImmutableSet<ServerConnectorFactory> connectorFactories;

  @Inject GameServer(
      Provider<Server> serverProvider,
      Set<ServerConnectorFactory> connectorFactories) {
    this.serverProvider = serverProvider;
    this.connectorFactories = ImmutableSet.copyOf(connectorFactories);
  }

  private void addConnectors(Server server) {
    int numAdded = 0;

    for (ServerConnectorFactory factory : connectorFactories) {
      Optional<ServerConnector> connector = factory.createServerConnector(server);
      if (connector.isPresent()) {
        numAdded++;
        server.addConnector(connector.get());
      }
    }

    Preconditions.checkState(numAdded > 0, "You have to add at least one connector");
  }

  @VisibleForTesting ServletContextHandler createContextHandler(
      Server server, String path, int options) {
    return new ServletContextHandler(server, path, options);
  }

  @VisibleForTesting void startAndJoin(Server server) throws Exception {
    server.start();
    server.join();
  }

  /**
   * Starts the game server and waits for the server to complete.
   *
   * @throws Exception if the server fails to start.
   */
  void start() throws Exception {
    Server server = serverProvider.get();

    addConnectors(server);

    ServletContextHandler servletContextHandler =
        createContextHandler(server, "/", ServletContextHandler.SESSIONS);
    servletContextHandler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));

    // You MUST add DefaultServlet or your server will always return 404s
    servletContextHandler.addServlet(DefaultServlet.class, "/");

    startAndJoin(server);
  }
}
