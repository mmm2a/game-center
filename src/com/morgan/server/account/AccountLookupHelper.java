package com.morgan.server.account;

import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.morgan.server.auth.UserInformation;
import com.morgan.shared.common.BackendException;

/**
 * Helper that can look up user accounts.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface AccountLookupHelper {

  /**
   * Look's up a set of users.
   */
  ImmutableMap<Long, UserInformation> lookUpAccounts(Set<Long> ids) throws BackendException;
}
