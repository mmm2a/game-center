package com.morgan.server.staticres;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morgan.server.staticres.StaticResourcesManager.ResourceSource;

/**
 * {@link HttpServlet} for serving static resources.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
class StaticResourcesHttpServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  private static final Pattern PATH_PATTERN = Pattern.compile(
      "^/([^/]+)/([^/]+)$");

  private final StaticResourcesManager manager;

  @Inject StaticResourcesHttpServlet(StaticResourcesManager manager) {
    this.manager = manager;
  }

  @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String path = req.getPathInfo();
    Matcher matcher = PATH_PATTERN.matcher(path);
    if (!matcher.matches()) {
      resp.setStatus(HttpStatus.NOT_FOUND_404);
    } else {
      String context = matcher.group(1);
      String resource = matcher.group(2);
      ResourceSource source = manager.getResource(context, resource);
      if (source == null) {
        resp.setStatus(HttpStatus.NOT_FOUND_404);
      } else {
        source.doGet(resp);
      }
    }
  }
}
