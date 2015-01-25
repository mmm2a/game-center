package com.morgan.client.auth;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.morgan.client.page.PagePresenter;
import com.morgan.shared.auth.AuthApplicationPlace;

/**
 * {@link PagePresenter} for auth application page.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class AuthPagePresenter implements PagePresenter<AuthApplicationPlace> {

  private final Provider<LoginPagePresenter> loginPagePresenterProvider;

  @Inject AuthPagePresenter(Provider<LoginPagePresenter> loginPagePresenterProvider) {
    this.loginPagePresenterProvider = loginPagePresenterProvider;
  }

  @Override public IsWidget presentPageFor(AuthApplicationPlace place) {
    return loginPagePresenterProvider.get().asWidget();
  }
}
