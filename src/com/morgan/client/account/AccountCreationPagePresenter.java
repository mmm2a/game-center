package com.morgan.client.account;

import javax.annotation.Nullable;

import com.google.common.base.Optional;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.morgan.client.alert.AlertController;
import com.morgan.client.constants.ClientPageConstants;
import com.morgan.client.page.PagePresenter;
import com.morgan.shared.account.AccountCreationApplicationPlace;
import com.morgan.shared.auth.AuthenticationConstant;

/**
 * {@link PagePresenter} for account creation.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class AccountCreationPagePresenter
    implements PagePresenter<AccountCreationApplicationPlace> {

  /**
   * View interface for the account creation page.
   *
   * @author mark@mark-morgan.net (Mark Morgan)
   */
  @ImplementedBy(DefaultNewAccountView.class)
  interface View extends IsWidget {
    /** Gets the email address currently entered in the view */
    String getEmailAddress();

    /** Gets the display name currently entered in the view */
    String getDisplayName();

    /** Gets a reference to the control that the user clicks to activate the account creation. */
    HasClickHandlers getCreateControl();
  }

  private final AccountMessages messages;
  private final AlertController alerts;
  @Nullable private final View view;

  @Inject AccountCreationPagePresenter(
      AccountMessages messages,
      AlertController alerts,
      View view,
      ClientPageConstants constants) {
    this.messages = messages;
    this.alerts = alerts;

    if (constants.getBoolean(AuthenticationConstant.IS_ADMIN)) {
      this.view = view;
    } else {
      this.view = null;
    }
  }

  @Override public Optional<? extends IsWidget> presentPageFor(
      AccountCreationApplicationPlace place) {
    if (view == null) {
      alerts.newErrorAlertBuilder(messages.accountCreationNotPermitted())
          .create()
          .requestDisplay();
    }

    return Optional.fromNullable(view);
  }
}
