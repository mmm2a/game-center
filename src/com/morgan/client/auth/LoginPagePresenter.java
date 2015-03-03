package com.morgan.client.auth;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.morgan.client.alert.AlertController;
import com.morgan.shared.auth.AuthenticationServiceAsync;

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
    /** Retrieves the email address currently entered in the text field. */
    String getEmailAddress();

    /** Retrieves the password currently entered in the text field. */
    String getPassword();

    /** Gets a handle to the control used to select the login option. */
    HasClickHandlers getLoginButtonControl();
  }

  private final ClickHandler clickHandler = new ClickHandler() {
    @Override public void onClick(ClickEvent event) {
      doLogin();
    }
  };

  private final AsyncCallback<Boolean> loginCallback = new AsyncCallback<Boolean>() {
    @Override public void onSuccess(Boolean result) {
      if (result) {
        handleLoginSucceeded();
      } else {
        handleLoginFailed();
      }
    }

    @Override public void onFailure(Throwable caught) {
      handleLoginError(caught);
    }
  };

  private final AlertController alertController;
  private final LoginPageView view;
  private final AuthenticationServiceAsync authService;
  private final AuthenticationMessages messages;

  @Inject LoginPagePresenter(
      AuthenticationMessages messages,
      AlertController alertController,
      AuthenticationServiceAsync authService,
      LoginPageView view) {
    this.messages = messages;
    this.alertController = alertController;
    this.view = view;
    this.authService = authService;

    view.getLoginButtonControl().addClickHandler(clickHandler);
  }

  private void doLogin() {
    authService.authenticate(
        view.getEmailAddress(),
        view.getPassword(),
        alertController.newStatusAlertBuilder(messages.loggingInStatus())
            .isFading(false)
            .create()
            .requestDisplay()
            .delegateTo(loginCallback));
  }

  private void handleLoginSucceeded() {
    Window.alert("Login succeeded");
  }

  private void handleLoginFailed() {
    alertController.newErrorAlertBuilder(messages.logInFailed())
        .create()
        .requestDisplay();
  }

  private void handleLoginError(Throwable caught) {
    alertController.newErrorAlertBuilder(messages.logInError())
        .create()
        .requestDisplay();
  }

  @Override public Widget asWidget() {
    return view.asWidget();
  }
}
