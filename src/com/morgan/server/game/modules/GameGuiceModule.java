package com.morgan.server.game.modules;

import com.google.inject.AbstractModule;
import com.morgan.server.game.modules.testgames.TestGamesModule;

/**
 * The GUICE module that configures the individual game modules.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class GameGuiceModule extends AbstractModule {

  @Override protected void configure() {
    install(new TestGamesModule());
  }
}
