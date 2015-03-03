package com.morgan.client.auth;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.morgan.client.alert.AlertController;
import com.morgan.client.constants.ClientPageConstants;
import com.morgan.client.nav.Navigator;
import com.morgan.shared.auth.AuthenticationConstant;
import com.morgan.shared.auth.AuthenticationServiceAsync;

/**
 * Page widget for displaying the logout page.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class LogoutPagePresenter implements IsWidget {

  /**
   * View for the {@link LogoutPagePresenter}.
   */
  @ImplementedBy(DefaultLogoutPageView.class)
  interface LogoutPageView extends IsWidget {
    /**
     * Sets the email address that is currently logged in.
     */
    void setEmailAddress(String emailAddress);

    /**
     * Gets the control that is used to activate the "logout" action.
     */
    HasClickHandlers getLogoutControl();
  }

  private final ClickHandler logoutHandler = new ClickHandler() {
    @Override public void onClick(ClickEvent event) {
      doLogout();
    }
  };

  private final AsyncCallback<Void> logoutCallback = new AsyncCallback<Void>() {
    @Override public void onSuccess(@Nullable Void result) {
      handleLogoutSuccess();
    }

    @Override public void onFailure(Throwable caught) {
      handleLogoutFailure(caught);
    }
  };

  private final AuthenticationMessages messages;
  private final AlertController alertController;
  private final AuthenticationServiceAsync authService;
  private final LogoutPageView view;
  private final Navigator navigator;

  @Inject LogoutPagePresenter(
      AuthenticationMessages messages,
      AlertController alertController,
      AuthenticationServiceAsync authService,
      ClientPageConstants constants,
      LogoutPageView view,
      Navigator navigator) {
    this.messages = messages;
    this.alertController = alertController;
    this.authService = authService;
    this.view = view;
    this.navigator = navigator;

    String email = constants.getString(AuthenticationConstant.CURRENT_USER_EMAIL);
    Preconditions.checkState(!Strings.isNullOrEmpty(email));

    view.setEmailAddress(email);
    view.getLogoutControl().addClickHandler(logoutHandler);
  }

  private void doLogout() {
    authService.logout(alertController.newStatusAlertBuilder(messages.loggingOutStatus())
        .isFading(false)
        .create()
        .requestDisplay()
        .delegateTo(logoutCallback));
  }

  private void handleLogoutSuccess() {
    navigator.reload();
  }

  private void handleLogoutFailure(Throwable caught) {
    alertController.newErrorAlertBuilder(messages.logOutFailed())
        .create()
        .requestDisplay();
  }

  @Override public Widget asWidget() {
    return view.asWidget();
  }
}
