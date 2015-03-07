package com.morgan.shared.account;

import com.morgan.shared.game.BaseGameApplicationPlace;
import com.morgan.shared.nav.ApplicationPlace;

/**
 * {@link ApplicationPlace} type for the account creation page.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AccountCreationApplicationPlace extends BaseGameApplicationPlace {

  public AccountCreationApplicationPlace() {
  }

  @Override public boolean equals(Object o) {
    return super.equals(o) && (o instanceof AccountCreationApplicationPlace);
  }

  @Override public AccountCreationPlaceRepresentation getRepresentation() {
    return new AccountCreationPlaceRepresentation();
  }
}
