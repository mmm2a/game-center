package com.morgan.server.game;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

import com.google.common.base.Optional;

/**
 * Interface for a type that can create a connector for the Jetty server.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
interface ServerConnectorFactory {

  /**
   * Asks this factory to try and create a server connector.  A return value of
   * {@link Optional#absent()} means that this factory declines to do so.
   */
  Optional<ServerConnector> createServerConnector(Server server);
}
