package com.morgan.server.auth;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Servlet implementation for serving the authentication host page.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
class AuthHostPageServlet extends HttpServlet {

  static final long serialVersionUID = 0L;

  private final AuthSoyTemplate soy;

  @Inject AuthHostPageServlet(
      AuthSoyTemplate soy) {
    this.soy = soy;
  }

  @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    try (Writer out = resp.getWriter()) {
      soy.hostPage().render(out);
      resp.setContentType("text/html");
      resp.setStatus(HttpServletResponse.SC_OK);
    }
  }
}
