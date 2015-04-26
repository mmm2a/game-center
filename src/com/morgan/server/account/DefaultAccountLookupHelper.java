package com.morgan.server.account;

import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.morgan.server.auth.UserInformation;
import com.morgan.server.backend.UserBackend;
import com.morgan.shared.common.BackendException;

/**
 * Default implementation of the {@link AccountLookupHelper} interface.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class DefaultAccountLookupHelper implements AccountLookupHelper {

  private final UserBackend userBackend;

  @Inject DefaultAccountLookupHelper(UserBackend userBackend) {
    this.userBackend = userBackend;
  }

  @Override public ImmutableMap<Long, UserInformation> lookUpAccounts(Set<Long> ids)
      throws BackendException {
    if (ids.isEmpty()) {
      return ImmutableMap.of();
    }

    return userBackend.findUsersById(ids);
  }
}
