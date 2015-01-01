package com.morgan.server.security;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * I think that this is going to be a test class to get rid of later, but want to try some stuff
 * out with the servlet engine.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class SecurityInformationHelper {

  private final Provider<HttpServletRequest> requestProvider;
  private final CookieHelper cookieHelper;

  @Inject SecurityInformationHelper(Provider<HttpServletRequest> requestProvider,
      CookieHelper cookieHelper) {
    this.requestProvider = requestProvider;
    this.cookieHelper = cookieHelper;
  }

  public ImmutableMap<String, String> getInformation() {
    HttpServletRequest request = requestProvider.get();

    ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

    StringBuilder sBuilder = new StringBuilder();
    Enumeration<String> names = request.getAttributeNames();
    while (names.hasMoreElements()) {
      sBuilder.append(names.nextElement() + ", ");
    }
    builder.put("Attribute names", sBuilder.toString());

    builder.put("Context path", request.getContextPath());
    builder.put("Dispatcher type", request.getDispatcherType().toString());

    names = request.getHeaderNames();
    while (names.hasMoreElements()) {
      String name = names.nextElement();
      builder.put("Header[" + name + "]", request.getHeader(name));
    }

    builder.put("Local addr", request.getLocalAddr());
    builder.put("Locale", request.getLocale().toString());
    builder.put("Local name", request.getLocalName());
    builder.put("Local port", "" + request.getLocalPort());

    builder.put("Method", request.getMethod());
    builder.put("Parameter map", request.getParameterMap().toString());

    builder.put("Path info", "" + request.getPathInfo());
    builder.put("Path translated", "" + request.getPathTranslated());
    builder.put("Protocol", request.getProtocol());
    builder.put("Query string", "" + request.getQueryString());
    builder.put("Remote addr", request.getRemoteAddr());
    builder.put("Remote host", request.getRemoteHost());
    builder.put("Remote port", "" + request.getRemotePort());
    builder.put("Remote user", "" + request.getRemoteUser());

    builder.put("request uri", "" + request.getRequestURI());
    builder.put("request url", "" + request.getRequestURL());
    builder.put("scheme", "" + request.getScheme());
    builder.put("server name", "" + request.getServerName());
    builder.put("server port", "" + request.getServerPort());
    builder.put("servlet path", "" + request.getServletPath());

    Optional<Long> userId = cookieHelper.getUserIdFromCookie();
    if (userId.isPresent()) {
      builder.put("User ID", "" + userId.get());
    } else {
      builder.put("User ID", "Not logged in");
      cookieHelper.setAuthenticationCookieFor(69L);
    }

    return builder.build();
  }
}
