package com.morgan.server.polymer;

import com.google.inject.servlet.ServletModule;
import com.morgan.shared.game.polymer.PolymerConstants;

/**
 * {@link ServletModule} for the polymer package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class PolymerServletModule extends ServletModule {

  @Override protected void configureServlets() {
    serve(PolymerConstants.POLYMER_PATH_PREFIX + "*").with(PolymerComponentServlet.class);
  }
}
