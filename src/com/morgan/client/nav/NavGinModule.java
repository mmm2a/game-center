package com.morgan.client.nav;

import com.google.gwt.inject.client.AbstractGinModule;

/**
 * GIN module for the client nav package.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class NavGinModule extends AbstractGinModule {

  @Override protected void configure() {
    bind(DefaultNavigation.class).asEagerSingleton();
    bind(NavigationState.class).to(DefaultNavigation.class);
    bind(Navigator.class).to(DefaultNavigation.class);
  }
}
