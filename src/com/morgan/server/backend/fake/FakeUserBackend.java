package com.morgan.server.backend.fake;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morgan.server.auth.UserInformation;
import com.morgan.server.backend.UserBackend;
import com.morgan.shared.common.BackendException;
import com.morgan.shared.common.Role;

/**
 * Fake implementation of the {@link UserBackend} interface.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
class FakeUserBackend implements UserBackend {

  private final Map<Long, UserInformation> idToUserInformationMap = new HashMap<>();
	private final Map<String, UserInformation> emailToUserInformationMap = new HashMap<>();
	private final Map<Long, String> userIdToPasswordMap = new HashMap<>();

	private long nextUserId = 1L;

	@Inject FakeUserBackend() {
		addUser("Mark", "mark@mark-morgan.net", Role.ADMIN, "!!password");
	}

	synchronized private UserInformation addUser(
	    String name, String email, Role role, String password) {
		UserInformation userInfo = new UserInformation(nextUserId++, name, email, role);

		idToUserInformationMap.put(userInfo.getUserId(), userInfo);
		emailToUserInformationMap.put(email, userInfo);
		userIdToPasswordMap.put(userInfo.getUserId(), password);

		return userInfo;
	}

	@Override synchronized public Optional<UserInformation> logIn(
		String emailAddress, String password)
		    throws BackendException {
	  UserInformation info = emailToUserInformationMap.get(emailAddress);
	  if (info != null) {
		  String p = userIdToPasswordMap.get(info.getUserId());
		  if (p != null && p.equals(password)) {
			  return Optional.of(info);
		  }
	  }

	  return Optional.absent();
	}

  @Override synchronized public Optional<UserInformation> findUserById(
      long userId) throws BackendException {
    return Optional.fromNullable(idToUserInformationMap.get(userId));
  }

  @Override synchronized public ImmutableMap<Long, UserInformation> findUsersById(Set<Long> ids)
      throws BackendException {
    ImmutableMap.Builder<Long, UserInformation> resultBuilder = ImmutableMap.builder();
    for (long id : ids) {
      Optional<UserInformation> user = findUserById(id);
      if (user.isPresent()) {
        resultBuilder.put(id, user.get());
      }
    }

    return resultBuilder.build();
  }

  @Override synchronized public UserInformation createAccount(
      String emailAddress,
      String displayName,
      String password,
      Role role) throws BackendException {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(emailAddress));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(displayName));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(password));
    Preconditions.checkNotNull(role);

    if (emailToUserInformationMap.containsKey(emailAddress)) {
      throw new BackendException(
          String.format("User with email address %s already exists", emailAddress));
    }

    return addUser(displayName, emailAddress, role, password);
  }
}
