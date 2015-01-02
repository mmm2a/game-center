package com.morgan.server.backend;

import com.google.common.base.Optional;
import com.morgan.server.auth.UserInformation;
import com.morgan.server.common.BackendException;

/**
 * Interface for communicating with the backend to deal with user authentication issues.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface UserBackend {
	
	/**
	 * Try to log the current user in with the given email address and the given password.  
	 * Returns {@link Optional#absent()} if the user isn't able to log in with the given 
	 * information.
	 */
	Optional<UserInformation> logIn(String emailAddress, String password) throws BackendException;
}
