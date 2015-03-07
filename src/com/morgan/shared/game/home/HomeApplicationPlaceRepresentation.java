package com.morgan.shared.game.home;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.morgan.shared.nav.AbstractTokenBasedApplicationPlaceRepresentation;
import com.morgan.shared.nav.ApplicationPlace;
import com.morgan.shared.nav.ApplicationPlaceRepresentation;

/**
 * {@link ApplicationPlaceRepresentation} for the {@link HomeApplicationPlace} place type.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class HomeApplicationPlaceRepresentation
    extends AbstractTokenBasedApplicationPlaceRepresentation {

  private static final String TOKEN = "home";

  @Inject public HomeApplicationPlaceRepresentation() {
    super(TOKEN);
  }

  @Override @Nullable protected HomeApplicationPlace parseFromPartsAfterToken(
      ImmutableList<String> remainingParts,
      ImmutableMap<String, String> parameterMap) {
    return remainingParts.isEmpty() ? new HomeApplicationPlace() : null;
  }

  @Override protected Iterable<String> getPathPartsAfterTokenFor(ApplicationPlace place) {
    Preconditions.checkArgument(place instanceof HomeApplicationPlace);
    return ImmutableList.of();
  }
}
