package com.morgan.client.auth;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.morgan.client.constants.ClientPageConstants;
import com.morgan.client.page.PagePresenter;
import com.morgan.shared.auth.AuthApplicationPlace;
import com.morgan.shared.auth.AuthenticationConstant;

/**
 * {@link PagePresenter} for auth application page.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class AuthPagePresenter implements PagePresenter<AuthApplicationPlace> {

  private final ClientPageConstants constants;
  private final Provider<LoginPagePresenter> loginPagePresenterProvider;
  private final Provider<LogoutPagePresenter> logoutPagePresenterProvider;

  @Inject AuthPagePresenter(
      ClientPageConstants constants,
      Provider<LoginPagePresenter> loginPagePresenterProvider,
      Provider<LogoutPagePresenter> logoutPagePresenterProvider) {
    this.constants = constants;
    this.loginPagePresenterProvider = loginPagePresenterProvider;
    this.logoutPagePresenterProvider = logoutPagePresenterProvider;
  }

  @Override public Optional<? extends IsWidget> presentPageFor(AuthApplicationPlace place) {
    return Optional.of(constants.getBoolean(AuthenticationConstant.IS_LOGGED_IN)
        ? logoutPagePresenterProvider.get()
        : loginPagePresenterProvider.get());
  }
}
