package com.morgan.server.backend.prod;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.morgan.server.auth.UserInformation;
import com.morgan.server.backend.UserBackend;
import com.morgan.shared.common.BackendException;

/**
 * Production implementation of the {@link UserBackend} interface.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class ProdUserBackend implements UserBackend {

	@Inject ProdUserBackend() {
	}
	
	@Override public Optional<UserInformation> logIn(String emailAddress, String password)
        throws BackendException {
      throw new UnsupportedOperationException();
	}
}
