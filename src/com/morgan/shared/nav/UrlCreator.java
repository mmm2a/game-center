package com.morgan.shared.nav;

import com.google.gwt.safehtml.shared.SafeUri;


/**
 * A type that can create a URL for the server or the client.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface UrlCreator {
  /**
   * Creates and returns a full URL for the input place.
   */
  SafeUri createUrlFor(ApplicationPlace place);
}
