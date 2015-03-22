package com.morgan.server.game;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morgan.server.constants.PageConstantsHelper;
import com.morgan.server.util.stat.MeasureStatistics;

/**
 * Servlet implementation for serving the game host page.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
class GameHostPageServlet extends HttpServlet {

  static final long serialVersionUID = 0L;

  private final GameSoyTemplate soy;
  private final PageConstantsHelper pageConstantsHelper;

  @Inject GameHostPageServlet(
      GameSoyTemplate soy,
      PageConstantsHelper pageConstantsHelper) {
    this.soy = soy;
    this.pageConstantsHelper = pageConstantsHelper;
  }

  @MeasureStatistics
  @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    try (Writer out = resp.getWriter()) {
      resp.setContentType("text/html");
      resp.setStatus(HttpServletResponse.SC_OK);
      resp.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
      resp.setHeader("Cache-Control", "max-age=0"); //HTTP 1.1
      resp.setHeader("Cache-Control", "no-store"); //HTTP 1.1
      resp.setHeader("Pragma", "no-cache"); //HTTP 1.0
      resp.setDateHeader("Expires", 0); //prevents caching at the proxy server
      soy.hostPage(pageConstantsHelper.createPageConstantsJson()).render(out);
    }
  }
}
