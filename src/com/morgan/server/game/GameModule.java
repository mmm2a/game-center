package com.morgan.server.game;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.morgan.server.util.flag.FlagAccessorFactory;

/**
 * Main GUICE module for the game server.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class GameModule extends AbstractModule {

  @Override protected void configure() {
    install(new GameServletsModule());
  }

  @Provides @Singleton protected GameServerFlagAccessor provideGameServerFlagAccessor(
      FlagAccessorFactory accessorFactory) {
    return accessorFactory.getFlagAccessor(GameServerFlagAccessor.class);
  }
}
