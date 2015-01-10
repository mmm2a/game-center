package com.morgan.client.auth;

import com.google.gwt.inject.client.AbstractGinModule;
import com.morgan.client.common.CommonGinModule;

/**
 * GIN module for the authentication application.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AuthAppGinModule extends AbstractGinModule {

  @Override protected void configure() {
    install(new CommonGinModule());
  }
}
