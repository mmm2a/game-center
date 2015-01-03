package com.morgan.server.auth;

import com.google.inject.servlet.ServletModule;

/**
 * GUICE module for serving the authentication host page.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class AuthHostPageServletModule extends ServletModule {

  @Override protected void configureServlets() {
    serve("/apps/auth", "/apps/auth/").with(AuthHostPageServlet.class);
  }
}
