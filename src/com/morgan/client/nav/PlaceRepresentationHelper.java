package com.morgan.client.nav;

import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.morgan.client.common.CommonBindingAnnotations.Default;
import com.morgan.shared.nav.ApplicationPlace;
import com.morgan.shared.nav.ApplicationPlaceRepresentation;

/**
 * A helper class for helping parse and generate history representations for places.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class PlaceRepresentationHelper {

  private final ApplicationPlace defaultPlace;
  private final ImmutableSet<ApplicationPlaceRepresentation> placeRepresentations;

  @Inject PlaceRepresentationHelper(
      @Default ApplicationPlace defaultPlace,
      Set<ApplicationPlaceRepresentation> placeRepresentations) {
    this.defaultPlace = defaultPlace;
    this.placeRepresentations = ImmutableSet.copyOf(placeRepresentations);
  }

  /**
   * Try to parse the history token provided and find the appropriate matching
   * {@link ApplicationPlace}.  If the history token can't be found, then {@code null} is
   * returned.
   */
  @Nullable ApplicationPlace parseFromHistoryToken(String historyToken) {
    Preconditions.checkNotNull(historyToken);
    for (ApplicationPlaceRepresentation representation : placeRepresentations) {
      ApplicationPlace parsedPlace = representation.parsePlaceFromToken(historyToken);
      if (parsedPlace != null) {
        return parsedPlace;
      }
    }

    return defaultPlace;
  }

  /**
   * Generates the string representation of a place.
   */
  String representPlaceAsHistoryToken(ApplicationPlace place) {
    Preconditions.checkNotNull(place);
    return place.getRepresentation().generateUrlTokenFor(place);
  }
}
