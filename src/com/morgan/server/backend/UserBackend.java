package com.morgan.server.backend;

import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.morgan.server.auth.UserInformation;
import com.morgan.shared.common.BackendException;
import com.morgan.shared.common.Role;

/**
 * Interface for communicating with the backend to deal with user authentication issues.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface UserBackend {

  /**
   * Create a new user account.
   */
  UserInformation createAccount(
      String emailAddress, String displayName, String password, Role role) throws BackendException;

	/**
	 * Try to log the current user in with the given email address and the given password.
	 * Returns {@link Optional#absent()} if the user isn't able to log in with the given
	 * information.
	 */
	Optional<UserInformation> logIn(String emailAddress, String password) throws BackendException;

	/**
	 * Look's up the given user by his/her id.  If the user can't be found, returns
	 * {@link Optional#absent()}.
	 */
	Optional<UserInformation> findUserById(long userId) throws BackendException;

	/**
	 * Look's up a bunch of user's by their IDs and returns a map of the results.  If a user can't
	 * be found, then no result is returned for that user.
	 */
  ImmutableMap<Long, UserInformation> findUsersById(Set<Long> ids) throws BackendException;
}
