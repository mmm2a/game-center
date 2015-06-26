package com.morgan.tools;

import com.google.inject.AbstractModule;
import com.morgan.server.backend.BackendModule;
import com.morgan.server.util.log.LogModule;

/**
 * GUICE module for tools.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class ToolModule extends AbstractModule {

  @Override protected void configure() {
    install(new BackendModule());
    install(new LogModule());
  }
}
