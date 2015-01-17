package com.morgan.shared.auth;

import com.morgan.shared.nav.ApplicationPlace;

/**
 * {@link ApplicationPlace} for logging in.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class LoginApplicationPlace extends AuthenticationApplicationPlace {

  public LoginApplicationPlace() {
  }

  @Override public boolean equals(Object o) {
    return super.equals(o) && (o instanceof LoginApplicationPlace);
  }

  @Override public LoginApplicationPlaceRepresentation getRepresentation() {
    return new LoginApplicationPlaceRepresentation();
  }
}
