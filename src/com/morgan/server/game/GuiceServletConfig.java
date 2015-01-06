package com.morgan.server.game;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.morgan.server.util.common.ServiceStarter;

/**
 * Configuration listener for the grid web app.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class GuiceServletConfig extends GuiceServletContextListener {
  @Override protected Injector getInjector() {
    Injector injector = Guice.createInjector(new GameModule());

    // We start services here before returning
    injector.getInstance(ServiceStarter.class).startServices();

    return injector;
  }
}
