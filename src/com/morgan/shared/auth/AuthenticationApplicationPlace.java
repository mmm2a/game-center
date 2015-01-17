package com.morgan.shared.auth;

import java.util.Map;

import com.morgan.shared.nav.ApplicationPlace;
import com.morgan.shared.nav.ClientApplication;

/**
 * Abstract base class for all {@link ApplicationPlace} types that are in the authentication app.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public abstract class AuthenticationApplicationPlace extends ApplicationPlace {

  protected AuthenticationApplicationPlace(Map<String, String> parameters) {
    super(ClientApplication.AUTHENTICATION, parameters);
  }

  protected AuthenticationApplicationPlace() {
    super(ClientApplication.AUTHENTICATION);
  }
}
