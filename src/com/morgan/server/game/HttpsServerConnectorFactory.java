package com.morgan.server.game;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Implementation of the {@link ServerConnectorFactory} that creates an HTTPS server connector.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class HttpsServerConnectorFactory implements ServerConnectorFactory {

  private final GameServerFlagAccessor flagAccessor;
  private final Provider<SslContextFactory> sslContextFactoryProvider;

  @Inject HttpsServerConnectorFactory(
      GameServerFlagAccessor flagAccessor, Provider<SslContextFactory> sslContextFactoryProvider) {
    this.flagAccessor = flagAccessor;
    this.sslContextFactoryProvider = sslContextFactoryProvider;
  }

  @Override public Optional<ServerConnector> createServerConnector(Server server) {
    if (!flagAccessor.isSecure()) {
      return Optional.absent();
    }

    HttpConfiguration httpConfig = new HttpConfiguration();
    httpConfig.setOutputBufferSize(32768);

    HttpConfiguration httpsConfig = new HttpConfiguration(httpConfig);
    httpsConfig.addCustomizer(new SecureRequestCustomizer());

    ServerConnector https = new ServerConnector(server,
        new SslConnectionFactory(sslContextFactoryProvider.get(), "http/1.1"),
        new HttpConnectionFactory(httpsConfig));
    https.setPort(flagAccessor.port());
    https.setIdleTimeout(500000);

    return Optional.of(https);
  }
}
