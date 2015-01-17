package com.morgan.shared.auth;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.morgan.shared.nav.AbstractTokenBasedApplicationPlaceRepresentation;
import com.morgan.shared.nav.ApplicationPlace;
import com.morgan.shared.nav.ApplicationPlaceRepresentation;

/**
 * {@link ApplicationPlaceRepresentation} for the {@link LoginApplicationPlace} place type.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class LoginApplicationPlaceRepresentation
    extends AbstractTokenBasedApplicationPlaceRepresentation {

  private static final String TOKEN = "login";

  @Inject public LoginApplicationPlaceRepresentation() {
    super(TOKEN);
  }

  @Override protected LoginApplicationPlace parseFromPartsAfterToken(
      ImmutableList<String> remainingParts,
      ImmutableMap<String, String> parameterMap) {
    return remainingParts.isEmpty() ? new LoginApplicationPlace() : null;
  }

  @Override protected Iterable<String> getPathPartsAfterTokenFor(ApplicationPlace place) {
    Preconditions.checkArgument(place instanceof LoginApplicationPlace);
    return ImmutableList.of();
  }
}
