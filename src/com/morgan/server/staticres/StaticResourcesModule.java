package com.morgan.server.staticres;

import com.google.inject.AbstractModule;

/**
 * GUICE module for configuring the static resources package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class StaticResourcesModule extends AbstractModule {

  @Override protected void configure() {
    install(new StaticResourcesServletModule());

    bind(StaticResourcesFactory.class).to(StaticResourcesManager.class);
  }
}
