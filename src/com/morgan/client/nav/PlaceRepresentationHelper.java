package com.morgan.client.nav;

import javax.annotation.Nullable;

import com.google.inject.Inject;
import com.morgan.shared.nav.ApplicationPlace;

/**
 * A helper class for helping parse and generate history representations for places.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class PlaceRepresentationHelper {

  @Inject PlaceRepresentationHelper() {
  }
  
  /**
   * Try to parse the history token provided and find the appropriate matching
   * {@link ApplicationPlace}.  If the history token can't be found, then {@code null} is
   * returned.
   */
  @Nullable ApplicationPlace parseFromHistoryToken(String historyToken) {
    // TODO(morgan): Implement
    return null;
  }
  
  /**
   * Generates the string representation of a place.
   */
  String representPlaceAsHistoryToken(ApplicationPlace place) {
    // TODO(morgan): Implement
    return null;
  }
}
