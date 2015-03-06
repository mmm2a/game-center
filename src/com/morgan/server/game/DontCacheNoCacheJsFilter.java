package com.morgan.server.game;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * A {@link Filter} that sets the approriate headers in the HTTP response to not cache the
 * *.nocache.js files.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
class DontCacheNoCacheJsFilter implements Filter {

  @Inject DontCacheNoCacheJsFilter() {
  }

  @Override public void destroy() {
    // Nothing needs to be done
  }

  @Override public void doFilter(
      ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {
    Preconditions.checkArgument(response instanceof HttpServletResponse);

    System.err.format("Filtering request for: %s\n", ((HttpServletRequest)request ).getServletPath());

    HttpServletResponse resp = (HttpServletResponse) response;

    resp.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
    resp.setHeader("Cache-Control", "max-age=0"); //HTTP 1.1
    resp.setHeader("Cache-Control", "no-store"); //HTTP 1.1
    resp.setHeader("Pragma", "no-cache"); //HTTP 1.0
    resp.setDateHeader("Expires", 0); //prevents caching at the proxy server

    chain.doFilter(request, response);
  }

  @Override public void init(FilterConfig config) throws ServletException {
    // Nothing needs to be done
  }
}
