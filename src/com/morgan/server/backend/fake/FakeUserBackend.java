package com.morgan.server.backend.fake;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morgan.server.auth.UserInformation;
import com.morgan.server.backend.UserBackend;
import com.morgan.server.common.BackendException;
import com.morgan.shared.common.Role;

/**
 * Fake implementation of the {@link UserBackend} interface.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
class FakeUserBackend implements UserBackend {

	private final Map<String, UserInformation> emailToUserInformationMap = new HashMap<>();
	private final Map<Long, String> userIdToPasswordMap = new HashMap<>();
	
	private long nextUserId = 1L;
	
	@Inject FakeUserBackend() {
		addUser("Mark", "mark@mark-morgan.net", Role.ADMIN, "!!password");
	}
	
	synchronized private void addUser(String name, String email, Role role, String password) {
		UserInformation userInfo = new UserInformation(nextUserId++, name, email, role);
		emailToUserInformationMap.put(email, userInfo);
		userIdToPasswordMap.put(userInfo.getUserId(), password);
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
}
