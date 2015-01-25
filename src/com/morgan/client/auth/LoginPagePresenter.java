package com.morgan.client.auth;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

/**
 * Page widget for displaying the login page.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class LoginPagePresenter implements IsWidget {

  /**
   * View for the {@link LoginPagePresenter}.
   */
  @ImplementedBy(DefaultLoginPageView.class)
  interface LoginPageView extends IsWidget {
  }

  private final LoginPageView view;

  @Inject LoginPagePresenter(LoginPageView view) {
    this.view = view;
  }

  @Override public Widget asWidget() {
    return view.asWidget();
  }
}
