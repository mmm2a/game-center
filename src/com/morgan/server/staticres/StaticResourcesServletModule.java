package com.morgan.server.staticres;

import com.google.inject.servlet.ServletModule;

/**
 * {@link ServletModule} for the static resources package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class StaticResourcesServletModule extends ServletModule {

  @Override protected void configureServlets() {
    serve(StaticResourcesManager.PATH_PREFIX + "*").with(StaticResourcesHttpServlet.class);
  }
}
