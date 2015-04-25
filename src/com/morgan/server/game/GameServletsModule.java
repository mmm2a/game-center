package com.morgan.server.game;

import com.google.inject.servlet.ServletModule;
import com.morgan.server.auth.LogInFilter;
import com.morgan.shared.game.GameService;

/**
 * GUICE servlets module for the game engine.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class GameServletsModule extends ServletModule {

  @Override protected void configureServlets() {
    super.configureServlets();

    filterRegex(".*.nocache.js$").through(DontCacheNoCacheJsFilter.class);
    filter("/apps/game").through(LogInFilter.class);

    bind(GameService.class).to(DefaultGameService.class);
    serve("/services/game-service").with(DefaultGameService.class);
  }
}
