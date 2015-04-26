package com.morgan.server.backend.prod;

import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.morgan.server.auth.UserInformation;
import com.morgan.server.backend.UserBackend;
import com.morgan.server.backend.prod.authdb.AuthDbHelper;
import com.morgan.shared.common.BackendException;
import com.morgan.shared.common.Role;

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

  @Override public Optional<UserInformation> findUserById(long userId) throws BackendException {
    return authDbHelper.findUserById(userId);
  }

  @Override public ImmutableMap<Long, UserInformation> findUsersById(Set<Long> ids)
      throws BackendException {
    return authDbHelper.findUsersById(ids);
  }

  @Override public UserInformation createAccount(
      String emailAddress,
      String displayName,
      String password,
      Role memberRole) throws BackendException {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(emailAddress));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(displayName));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(password));
    Preconditions.checkNotNull(memberRole);

    return authDbHelper.createAccount(emailAddress, displayName, password, memberRole);
  }
}
