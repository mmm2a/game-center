package com.morgan.server.polymer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.CharMatcher;
import com.google.common.base.Function;
import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import com.google.common.net.MediaType;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morgan.server.util.log.AdvancedLogger;
import com.morgan.server.util.log.InjectLogger;
import com.morgan.server.util.mime.MediaTypeMapper;

/**
 * Servlet for serving the polymer components (located under the accompanying resources folder).
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
class PolymerComponentServlet extends HttpServlet {

  static final long serialVersionUID = 1L;

  private static final CharMatcher LEADING_SLASH_TRIMMER = CharMatcher.is('/');

  @InjectLogger AdvancedLogger log = AdvancedLogger.NULL;

  private final Function<String, MediaType> contentTypeMapper;

  @Inject PolymerComponentServlet(@MediaTypeMapper Function<String, MediaType> contentTypeMapper) {
    this.contentTypeMapper = contentTypeMapper;
  }

  private static String getFileNameForPath(String path) {
    int i = path.lastIndexOf('/');
    if (i >= 0) {
      return path.substring(i + 1);
    }

    return path;
  }

  @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String relativePath = LEADING_SLASH_TRIMMER.trimLeadingFrom(req.getPathInfo());

    URL resourceUrl = Resources.getResource(
        PolymerComponentServlet.class, "resources/" + relativePath);
    ByteSource source = Resources.asByteSource(resourceUrl);

    log.debug("Attempting to get content type of path %s", relativePath);
    MediaType contentType = contentTypeMapper.apply(getFileNameForPath(relativePath));
    log.debug("Got back a content type of %s", contentType);
    try (OutputStream out = resp.getOutputStream()) {
      resp.setContentType(contentType.toString());

      source.copyTo(out);
    }
  }
}
