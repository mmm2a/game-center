package com.morgan.server.nav;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.morgan.server.constants.PageConstantsSource;

/**
 * GUICE module for the navigation package.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class NavigationModule extends AbstractModule {

  @Override protected void configure() {
    Multibinder.newSetBinder(binder(), PageConstantsSource.class)
        .addBinding().to(NavigationPageConstantsSource.class);
  }
}
