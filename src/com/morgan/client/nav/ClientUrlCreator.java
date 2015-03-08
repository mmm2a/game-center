package com.morgan.client.nav;

import com.google.common.base.Preconditions;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.inject.Inject;
import com.morgan.client.constants.ClientPageConstants;
import com.morgan.shared.nav.ApplicationPlace;
import com.morgan.shared.nav.ClientApplication;
import com.morgan.shared.nav.NavigationConstant;
import com.morgan.shared.nav.UrlCreator;

/**
 * Class for helping create URLs for places.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class ClientUrlCreator implements UrlCreator {

  private final ClientPageConstants constants;
  private final ClientApplication currentApplication;

  @Inject ClientUrlCreator(
      ClientPageConstants constants,
      ClientApplication currentApplication) {
    this.constants = constants;
    this.currentApplication = currentApplication;
  }

  /**
   * Creates and returns a full URL for the input place.
   */
  @Override
  public SafeUri createUrlFor(ApplicationPlace place) {
    String historyToken = place.getRepresentation().generateUrlTokenFor(place);
    String currentUrl = constants.getString(NavigationConstant.APPLICATION_URL);
    String expectedSuffix = "/" + currentApplication.getApplicationPathToken();
    Preconditions.checkState(currentUrl.endsWith(expectedSuffix));
    int index = currentUrl.lastIndexOf(expectedSuffix);
    return UriUtils.fromString(currentUrl.substring(0, index + 1)
        + place.getClientApplication().getApplicationPathToken()
        + "#" + historyToken);
  }
}
