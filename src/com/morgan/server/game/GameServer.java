package com.morgan.server.game;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceFilter;

/**
 * Main server class for the game engine.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
class GameServer {

  private final GameServerFlagAccessor flagAccessor;

  @Inject GameServer(GameServerFlagAccessor flagAccessor) {
    this.flagAccessor = flagAccessor;
  }

  /**
   * Starts the game server and waits for the server to complete.
   *
   * @throws Exception if the server fails to start.
   */
  void start() throws Exception {
    Server server = new Server(flagAccessor.port());

    ServletContextHandler servletContextHandler = new ServletContextHandler(
        server, "/", ServletContextHandler.SESSIONS);
    servletContextHandler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));

    // You MUST add DefaultServlet or your server will always return 404s
    servletContextHandler.addServlet(DefaultServlet.class, "/");

    // Start the server
    server.start();

    // Wait until the server exits
    server.join();
  }
}
