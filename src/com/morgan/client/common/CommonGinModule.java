package com.morgan.client.common;

import com.google.gwt.inject.client.AbstractGinModule;
import com.morgan.client.nav.NavGinModule;

/**
 * GIN module for packages common to all applications on the client.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class CommonGinModule extends AbstractGinModule {

  @Override protected void configure() {
    install(new NavGinModule());
  }
}
