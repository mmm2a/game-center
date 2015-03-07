package com.morgan.shared.account;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.morgan.shared.nav.AbstractTokenBasedApplicationPlaceRepresentation;
import com.morgan.shared.nav.ApplicationPlace;

/**
 * {@link ApplicationPlace} for creating new accounts.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AccountCreationPlaceRepresentation
    extends AbstractTokenBasedApplicationPlaceRepresentation {

  private static final String TOKEN = "newacct";

  @Inject public AccountCreationPlaceRepresentation() {
    super(TOKEN);
  }

  @Override public AccountCreationApplicationPlace parseFromPartsAfterToken(
      ImmutableList<String> remainingParts,
      ImmutableMap<String, String> parameterMap) {
    return remainingParts.isEmpty() ? new AccountCreationApplicationPlace() : null;
  }

  @Override protected Iterable<String> getPathPartsAfterTokenFor(ApplicationPlace place) {
    Preconditions.checkArgument(place instanceof AccountCreationApplicationPlace);

    return ImmutableList.of();
  }
}
