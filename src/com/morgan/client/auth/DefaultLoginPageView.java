package com.morgan.client.auth;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.morgan.client.auth.LoginPagePresenter.LoginPageView;

/**
 * Default implementation of the {@link LoginPageView} view interface.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class DefaultLoginPageView extends Composite implements LoginPageView {

  @UiTemplate("resources/LoginPage.ui.xml")
  interface Binder extends UiBinder<Widget, DefaultLoginPageView> {
  }

  private static final UiBinder<Widget, DefaultLoginPageView> binder =
      GWT.create(Binder.class);

  @UiField TextBox emailAddress;
  @UiField PasswordTextBox password;
  @UiField Button loginButton;

  @Inject DefaultLoginPageView(AuthResources resources) {
    resources.css().ensureInjected();

    initWidget(binder.createAndBindUi(this));
  }

  @Override public String getEmailAddress() {
    return emailAddress.getText();
  }

  @Override public String getPassword() {
    return password.getText();
  }

  @Override public HasClickHandlers getLoginButtonControl() {
    return loginButton;
  }
}
