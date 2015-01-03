package com.morgan.server.game;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.morgan.server.security.SecurityModule;
import com.morgan.server.util.flag.FlagAccessorFactory;

/**
 * A GUICE module that should ONLY be used for a bootstrap injector.  The main injector (which
 * will use the {@link GameModule} is created via the web application filters.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class BootstrapModule extends AbstractModule {

  @Override protected void configure() {
    install(new SecurityModule());

    Multibinder<ServerConnectorFactory> connectorFactoryBinder =
        Multibinder.newSetBinder(binder(), ServerConnectorFactory.class);
    connectorFactoryBinder.addBinding().to(HttpServerConnectorFactory.class);
    connectorFactoryBinder.addBinding().to(HttpsServerConnectorFactory.class);
  }

  @Provides @Singleton protected GameServerFlagAccessor provideGameServerFlagAccessor(
      FlagAccessorFactory accessorFactory) {
    return accessorFactory.getFlagAccessor(GameServerFlagAccessor.class);
  }
}
