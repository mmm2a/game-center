package com.morgan.client.auth;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;

/**
 * Main auth application.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class AuthApp {

  @Inject AuthApp() {
  }

  /**
   * Start the main application.
   */
  void startApplication() {
    // This method doesn't need to do anything
    // TODO(markmorgan): This is just temporary

    RootPanel.get().add(new Label("Hello from GWT"));
  }
}
