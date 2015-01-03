package com.morgan.server.game;

import com.google.inject.AbstractModule;
import com.morgan.server.auth.AuthModule;
import com.morgan.server.backend.BackendModule;
import com.morgan.server.security.SecurityModule;

/**
 * Main GUICE module for the game server.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class GameModule extends AbstractModule {

  @Override protected void configure() {
    install(new AuthModule());
    install(new GameServletsModule());
    install(new SecurityModule());
    install(new BackendModule());
  }
}
