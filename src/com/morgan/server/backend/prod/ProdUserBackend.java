package com.morgan.server.backend.prod;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.morgan.server.auth.UserInformation;
import com.morgan.server.backend.UserBackend;
import com.morgan.server.backend.prod.authdb.AuthDbHelper;

/**
 * Production implementation of the {@link UserBackend} interface.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class ProdUserBackend implements UserBackend {

  private final AuthDbHelper authDbHelper;

	@Inject ProdUserBackend(AuthDbHelper authDbHelper) {
	  this.authDbHelper = authDbHelper;
	}

	@Override public Optional<UserInformation> logIn(String emailAddress, String password) {
	  return authDbHelper.authenticate(emailAddress, password);
	}
}
