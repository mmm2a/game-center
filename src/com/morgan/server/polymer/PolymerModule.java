package com.morgan.server.polymer;

import com.google.inject.AbstractModule;

/**
 * GUICE module for the polymer package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class PolymerModule extends AbstractModule {

  @Override protected void configure() {
    install(new PolymerServletModule());
  }
}
