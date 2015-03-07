package com.morgan.server.game;

import com.google.inject.servlet.ServletModule;

/**
 * GUICE module for serving the game host page.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class GameHostPageServletModule extends ServletModule {

  @Override protected void configureServlets() {
    serve("/apps/game", "/apps/game/").with(GameHostPageServlet.class);
  }
}
