package com.morgan.server.auth;

import com.google.common.base.Optional;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morgan.server.backend.UserBackend;
import com.morgan.server.security.CookieHelper;
import com.morgan.server.util.log.AdvancedLogger;
import com.morgan.shared.auth.AuthenticationService;
import com.morgan.shared.common.BackendException;

/**
 * Default implementation of the {@link AuthenticationService} service.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
public class DefaultAuthenticationService extends RemoteServiceServlet
    implements AuthenticationService {

  static final long serialVersionUID = 1L;

  private static final AdvancedLogger log = new AdvancedLogger(DefaultAuthenticationService.class);

  private final UserBackend userBackend;
  private final CookieHelper cookieHelper;

  @Inject DefaultAuthenticationService(
      UserBackend userBackend,
      CookieHelper cookieHelper) {
    this.userBackend = userBackend;
    this.cookieHelper = cookieHelper;
  }

  @Override public boolean authenticate(
      String emailAddress, String password) throws BackendException {
    log.trace("User \"%s\" is attempting to log in", emailAddress);

    cookieHelper.invalidateCurrentCookie();
    Optional<UserInformation> userInfo = userBackend.logIn(emailAddress, password);
    if (userInfo.isPresent()) {
      cookieHelper.setAuthenticationCookieFor(userInfo.get().getUserId());
      log.trace(
          "User \"%s\" logged in successfully as user id %d",
          emailAddress, userInfo.get().getUserId());
    } else {
      log.warning("User \"%s\" tried to log in but failed to do so", emailAddress);
    }

    return userInfo.isPresent();
  }

  @Override public void logout() throws BackendException {
    cookieHelper.logout();
  }
}
