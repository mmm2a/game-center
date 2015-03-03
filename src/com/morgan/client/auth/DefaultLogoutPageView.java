package com.morgan.client.auth;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * Default {@link LogoutPagePresenter.LogoutPageView} implementation.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class DefaultLogoutPageView extends Composite implements LogoutPagePresenter.LogoutPageView {

  @UiTemplate("resources/LogoutPage.ui.xml")
  interface Binder extends UiBinder<Widget, DefaultLogoutPageView> {
  }

  private static final UiBinder<Widget, DefaultLogoutPageView> binder =
      GWT.create(Binder.class);

  @UiField SpanElement emailAddress;
  @UiField Button logoutButton;

  @Inject DefaultLogoutPageView(AuthResources resources) {
    resources.css().ensureInjected();

    initWidget(binder.createAndBindUi(this));
  }

  @Override public void setEmailAddress(String emailAddress) {
    this.emailAddress.setInnerText(emailAddress);
  }

  @Override public HasClickHandlers getLogoutControl() {
    return logoutButton;
  }
}
