package com.morgan.client.nav;

import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.morgan.shared.nav.ApplicationPlace;
import com.morgan.shared.nav.ApplicationPlaceRepresentation;

/**
 * A helper class for helping parse and generate history representations for places.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class PlaceRepresentationHelper {

  private final Provider<Set<ApplicationPlaceRepresentation>> placeRepresentationsProvider;

  @Inject PlaceRepresentationHelper(
      Provider<Set<ApplicationPlaceRepresentation>> placeRepresentationsProvider) {
    this.placeRepresentationsProvider = placeRepresentationsProvider;
  }

  /**
   * Try to parse the history token provided and find the appropriate matching
   * {@link ApplicationPlace}.  If the history token can't be found, then {@code null} is
   * returned.
   */
  @Nullable ApplicationPlace parseFromHistoryToken(String historyToken) {
    Preconditions.checkNotNull(historyToken);
    for (ApplicationPlaceRepresentation representation : placeRepresentationsProvider.get()) {
      ApplicationPlace parsedPlace = representation.parsePlaceFromToken(historyToken);
      if (parsedPlace != null) {
        return parsedPlace;
      }
    }

    return null;
  }

  /**
   * Generates the string representation of a place.
   */
  String representPlaceAsHistoryToken(ApplicationPlace place) {
    Preconditions.checkNotNull(place);
    return place.getRepresentation().generateUrlTokenFor(place);
  }
}
