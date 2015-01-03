package com.morgan.server.auth;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morgan.server.constants.PageConstantsHelper;
import com.morgan.shared.auth.AuthConstant;

/**
 * Servlet implementation for serving the authentication host page.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
class AuthHostPageServlet extends HttpServlet {

  static final long serialVersionUID = 0L;

  private final AuthSoyTemplate soy;
  private final PageConstantsHelper<AuthConstant> pageConstantsHelper;

  @Inject AuthHostPageServlet(
      AuthSoyTemplate soy,
      PageConstantsHelper<AuthConstant> pageConstantsHelper) {
    this.soy = soy;
    this.pageConstantsHelper = pageConstantsHelper;
  }

  @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    try (Writer out = resp.getWriter()) {
      soy.hostPage(pageConstantsHelper.createPageConstantsJson()).render(out);
      resp.setContentType("text/html");
      resp.setStatus(HttpServletResponse.SC_OK);
    }
  }
}
