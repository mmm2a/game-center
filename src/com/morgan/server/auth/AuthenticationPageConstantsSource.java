package com.morgan.server.auth;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.morgan.server.common.CommonBindingAnnotations.RequestUser;
import com.morgan.server.constants.PageConstants;
import com.morgan.server.constants.PageConstantsSource;
import com.morgan.shared.auth.AuthenticationConstant;
import com.morgan.shared.common.Role;

/**
 * {@link PageConstantsSource} for the authentication package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class AuthenticationPageConstantsSource implements PageConstantsSource {

  private final Provider<Optional<UserInformation>> userInformationProvider;

  @Inject AuthenticationPageConstantsSource(
      @RequestUser Provider<Optional<UserInformation>> userInformationProvider) {
    this.userInformationProvider = userInformationProvider;
  }

  @Override public void provideConstantsInto(PageConstants constantsSink) {
    Optional<UserInformation> userInfo = userInformationProvider.get();
    constantsSink.add(AuthenticationConstant.IS_LOGGED_IN, userInfo.isPresent());
    if (userInfo.isPresent()) {
      constantsSink.add(AuthenticationConstant.IS_ADMIN,
          userInfo.get().getUserRole() == Role.ADMIN);
      constantsSink.add(
          AuthenticationConstant.CURRENT_USER_DISPLAY_NAME, userInfo.get().getDisplayName());
      constantsSink.add(
          AuthenticationConstant.CURRENT_USER_EMAIL, userInfo.get().getEmailAddress());
    } else {
      constantsSink.add(AuthenticationConstant.IS_ADMIN, false);
    }
  }
}
