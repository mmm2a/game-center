package com.morgan.client.account;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * Default implementation of the {@link AccountCreationPagePresenter.View} view interface.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class DefaultNewAccountView extends Composite implements AccountCreationPagePresenter.View {

  @UiTemplate("resources/NewAccount.ui.xml")
  interface Binder extends UiBinder<Widget, DefaultNewAccountView> {
  }

  private static final UiBinder<Widget, DefaultNewAccountView> binder =
      GWT.create(Binder.class);

  @UiField TextBox emailAddress;
  @UiField TextBox displayName;
  @UiField Button createButton;

  @Inject DefaultNewAccountView(AccountResources resources) {
    resources.css().ensureInjected();

    initWidget(binder.createAndBindUi(this));
  }

  @Override public String getEmailAddress() {
    return emailAddress.getText();
  }

  @Override public String getDisplayName() {
    return displayName.getText();
  }

  @Override public HasClickHandlers getCreateControl() {
    return createButton;
  }
}
