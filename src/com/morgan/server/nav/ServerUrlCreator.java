package com.morgan.server.nav;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.morgan.server.util.log.AdvancedLogger;
import com.morgan.server.util.log.InjectLogger;
import com.morgan.shared.nav.ApplicationPlace;
import com.morgan.shared.nav.UrlCreator;

/**
 * Server implementation of the {@link UrlCreator} interface.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class ServerUrlCreator implements UrlCreator {

  private final Provider<HttpServletRequest> requestProvider;

  @InjectLogger private AdvancedLogger log = AdvancedLogger.NULL;

  @Inject ServerUrlCreator(Provider<HttpServletRequest> requestProvider) {
    this.requestProvider = requestProvider;
  }

  @Override public SafeUri createUrlFor(ApplicationPlace place) {
    HttpServletRequest request = requestProvider.get();

    try {
      URL url = new URL(request.getRequestURL().toString());
      return UriUtils.fromString(String.format("%s://%s:%d/apps/%s/#%s",
          url.getProtocol(),
          url.getHost(),
          url.getPort(),
          place.getClientApplication().getApplicationPathToken(),
          place.getRepresentation().generateUrlTokenFor(place)));
    } catch (MalformedURLException e) {
      // Shouldn't happen
      log.debug(e, "Unexpected error trying to parse %s as URL", request.getRequestURL());
      throw new IllegalStateException(e);
    }
  }
}
