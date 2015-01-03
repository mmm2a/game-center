package com.morgan.server.auth;

import com.google.inject.servlet.ServletModule;
import com.morgan.shared.auth.AuthenticationService;

/**
 * {@link ServletModule} GUICE module for the auth package's servlets.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class AuthServletsModule extends ServletModule {

  @Override protected void configureServlets() {
    bind(AuthenticationService.class).to(DefaultAuthenticationService.class);
    serve("/services/authentication-service").with(DefaultAuthenticationService.class);
  }
}
