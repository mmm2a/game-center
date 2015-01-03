package com.morgan.server.util.soy;

import com.google.inject.AbstractModule;
import com.google.template.soy.SoyModule;

/**
 * GUICE module for configuring the soy package.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class SoyUtilModule extends AbstractModule {

  @Override protected void configure() {
    install(new SoyModule());
  }
}
