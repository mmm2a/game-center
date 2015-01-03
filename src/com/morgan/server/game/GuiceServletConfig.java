package com.morgan.server.game;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Configuration listener for the grid web app.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class GuiceServletConfig extends GuiceServletContextListener {
  @Override protected Injector getInjector() {
    return Guice.createInjector(new GameModule());
  }
}
