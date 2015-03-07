package com.morgan.client.nav;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.morgan.client.constants.ClientPageConstants;
import com.morgan.shared.nav.ApplicationPlace;
import com.morgan.shared.nav.ClientApplication;
import com.morgan.shared.nav.NavigationConstant;

/**
 * Class for helping create URLs for places.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class UrlCreator {

  private final ClientPageConstants constants;
  private final ClientApplication currentApplication;
  private final PlaceRepresentationHelper representationHelper;

  @Inject UrlCreator(
      ClientPageConstants constants,
      ClientApplication currentApplication,
      PlaceRepresentationHelper representationHelper) {
    this.constants = constants;
    this.currentApplication = currentApplication;
    this.representationHelper = representationHelper;
  }

  /**
   * Creates and returns a full URL for the input place.
   */
  public String createUrlFor(ApplicationPlace place) {
    String historyToken = representationHelper.representPlaceAsHistoryToken(place);
    String currentUrl = constants.getString(NavigationConstant.APPLICATION_URL);
    String expectedSuffix = "/" + currentApplication.getApplicationPathToken();
    Preconditions.checkState(currentUrl.endsWith(expectedSuffix));
    int index = currentUrl.lastIndexOf(expectedSuffix);
    return currentUrl.substring(0, index + 1)
        + place.getClientApplication().getApplicationPathToken()
        + "#" + historyToken;
  }
}
