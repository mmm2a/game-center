package com.morgan.server.game;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

import com.google.common.base.Optional;
import com.google.inject.Inject;

/**
 * Default insecure (HTTP) version of the {@link ServerConnectorFactory}
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class HttpServerConnectorFactory implements ServerConnectorFactory {

  private final GameServerFlagAccessor flagAccessor;

  @Inject HttpServerConnectorFactory(GameServerFlagAccessor flagAccessor) {
    this.flagAccessor = flagAccessor;
  }

  @Override public Optional<ServerConnector> createServerConnector(Server server) {
    if (flagAccessor.isSecure()) {
      return Optional.absent();
    }

    HttpConfiguration httpConfig = new HttpConfiguration();
    httpConfig.setOutputBufferSize(32768);

    ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(httpConfig));
    http.setPort(flagAccessor.port());
    http.setIdleTimeout(30000);

    return Optional.of(http);
  }
}
