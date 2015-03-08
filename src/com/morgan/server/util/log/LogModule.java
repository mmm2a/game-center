package com.morgan.server.util.log;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 * GUICE module for logging infrastructure.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class LogModule extends AbstractModule {
  @Override protected void configure() {
    bindListener(Matchers.any(), new AdvancedLoggerTypeListener());
  }
}
