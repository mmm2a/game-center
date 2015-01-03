package com.morgan.client.auth;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.morgan.client.constants.ClientPageConstants;
import com.morgan.shared.auth.AuthConstant;
import com.morgan.shared.auth.AuthenticationServiceAsync;

/**
 * Main auth application.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class AuthApp {

  private final ClientPageConstants constants;
  private final AuthenticationServiceAsync service;

  @Inject AuthApp(ClientPageConstants constants, AuthenticationServiceAsync service) {
    this.constants = constants;
    this.service = service;
  }

  /**
   * Start the main application.
   */
  void startApplication() {
    // This method doesn't need to do anything
    // TODO(markmorgan): This is just temporary

    RootPanel.get().add(new Label(constants.getString(AuthConstant.HELLO_MSG) + ", Version = " + constants.getInt(AuthConstant.VERSION)));

    service.authenticate("mark@mark-morgan.net", "wrong password", new AsyncCallback<Boolean>() {
      @Override public void onSuccess(Boolean result) {
        RootPanel.get().add(new Label("Attempt to log in with \"wrong password\": " + result));
        service.authenticate("mark@mark-morgan.net", "!!password", new AsyncCallback<Boolean>() {
          @Override public void onSuccess(Boolean result) {
            RootPanel.get().add(new Label("Attempt to log in with \"!!password\": " + result));
          }

          @Override public void onFailure(Throwable caught) {
            RootPanel.get().add(new Label("Unable to contact service"));
          }
        });
      }

      @Override public void onFailure(Throwable caught) {
        RootPanel.get().add(new Label("Unable to contact service"));
      }
    });
  }
}
