package com.morgan.shared.auth;

import com.morgan.shared.nav.ApplicationPlace;

/**
 * {@link ApplicationPlace} for logging in.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AuthApplicationPlace extends AuthenticationApplicationPlace {

  public AuthApplicationPlace() {
  }

  @Override public boolean equals(Object o) {
    return super.equals(o) && (o instanceof AuthApplicationPlace);
  }

  @Override public AuthApplicationPlaceRepresentation getRepresentation() {
    return new AuthApplicationPlaceRepresentation();
  }
}
