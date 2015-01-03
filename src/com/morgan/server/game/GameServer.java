package com.morgan.server.game;

import java.util.Set;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Main server class for the game engine.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
class GameServer {

  private final GameServerFlagAccessor flagAccessor;
  private final Provider<Server> serverProvider;
  private final Provider<WebAppContext> webAppContextProvider;
  private final ImmutableSet<ServerConnectorFactory> connectorFactories;

  @Inject GameServer(
      GameServerFlagAccessor flagAccessor,
      Provider<Server> serverProvider,
      Provider<WebAppContext> webAppContextProvider,
      Set<ServerConnectorFactory> connectorFactories) {
    this.flagAccessor = flagAccessor;
    this.serverProvider = serverProvider;
    this.webAppContextProvider = webAppContextProvider;
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

  private Handler createAndConfigureGwtWebApp() {
    WebAppContext handler = webAppContextProvider.get();

    handler.setContextPath(flagAccessor.warContextPath());

    String warFile = flagAccessor.warFile();
    if (!Strings.isNullOrEmpty(warFile)) {
      // For when we are packaged as a WAR
      handler.setWar(warFile);
    } else {
      // For when we aren't packaged as a WAR
      handler.setResourceBase(flagAccessor.warResourceBase());
      handler.setDescriptor(flagAccessor.warDescriptorPath());
      handler.setParentLoaderPriority(flagAccessor.isParentLoaderPriority());
    }

    return handler;
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

    server.setHandler(createAndConfigureGwtWebApp());

    startAndJoin(server);
  }
}
