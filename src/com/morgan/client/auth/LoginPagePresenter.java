package com.morgan.client.auth;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import com.morgan.client.alert.AlertController;
import com.morgan.client.page.PagePresenter;
import com.morgan.shared.auth.AuthenticationServiceAsync;
import com.morgan.shared.auth.LoginApplicationPlace;

/**
 * A {@link PagePresenter} for presenting the login page.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class LoginPagePresenter implements PagePresenter<LoginApplicationPlace> {

  private final AlertController alerts;
  private final FlowPanel flowPanel = new FlowPanel();
  private final AuthenticationServiceAsync service;

  @Inject LoginPagePresenter(AlertController alerts, AuthenticationServiceAsync service) {
    this.alerts = alerts;
    this.service = service;
  }

  @Override public IsWidget presentPageFor(LoginApplicationPlace place) {

    alerts.newStatusAlertBuilder("Some status text").create().requestDisplay();
    alerts.newErrorAlertBuilder("An error message!").create().requestDisplay();

    service.authenticate("mark@mark-morgan.net", "wrong password", new AsyncCallback<Boolean>() {
      @Override public void onSuccess(Boolean result) {
        flowPanel.add(new Label("Attempt to log in with \"wrong password\": " + result));
        service.authenticate("mark@mark-morgan.net", "!!password", new AsyncCallback<Boolean>() {
          @Override public void onSuccess(Boolean result) {
            flowPanel.add(new Label("Attempt to log in with \"!!password\": " + result));
          }

          @Override public void onFailure(Throwable caught) {
            flowPanel.add(new Label("Unable to contact service"));
          }
        });
      }

      @Override public void onFailure(Throwable caught) {
        flowPanel.add(new Label("Unable to contact service"));
      }
    });

    return flowPanel;
  }
}
