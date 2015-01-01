package com.morgan.server.game;

import com.google.inject.servlet.ServletModule;

/**
 * GUICE servlets module for the game engine.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class GameServletsModule extends ServletModule {

  @Override protected void configureServlets() {
    super.configureServlets();

    serve("/hello", "/hello/*").with(HelloWorldServlet.class);
  }
}
