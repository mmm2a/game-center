package com.morgan.client.auth;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.morgan.client.constants.ClientPageConstants;
import com.morgan.shared.auth.AuthConstant;

/**
 * Main auth application.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class AuthApp {

  private final ClientPageConstants constants;

  @Inject AuthApp(ClientPageConstants constants) {
    this.constants = constants;
  }

  /**
   * Start the main application.
   */
  void startApplication() {
    // This method doesn't need to do anything
    // TODO(markmorgan): This is just temporary

    RootPanel.get().add(new Label(constants.getString(AuthConstant.HELLO_MSG) + ", Version = " + constants.getInt(AuthConstant.VERSION)));
  }
}
