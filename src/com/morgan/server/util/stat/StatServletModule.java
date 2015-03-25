package com.morgan.server.util.stat;

import com.google.inject.servlet.ServletModule;

/**
 * GUICE Servlet module for the stat package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class StatServletModule extends ServletModule {

  @Override protected void configureServlets() {
    super.configureServlets();

    serve("/example-chart.png").with(StatChartServlet.class);
  }
}
