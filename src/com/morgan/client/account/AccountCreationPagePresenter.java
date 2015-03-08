package com.morgan.client.account;

import javax.annotation.Nullable;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.morgan.client.alert.AlertController;
import com.morgan.client.constants.ClientPageConstants;
import com.morgan.client.nav.Navigator;
import com.morgan.client.page.PagePresenter;
import com.morgan.shared.account.AccountCreationApplicationPlace;
import com.morgan.shared.account.AccountServiceAsync;
import com.morgan.shared.auth.AuthenticationConstant;
import com.morgan.shared.auth.ClientUserInformation;
import com.morgan.shared.common.Role;
import com.morgan.shared.game.home.HomeApplicationPlace;

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

  private final ClickHandler createHandler = new ClickHandler() {
    @Override public void onClick(ClickEvent e) {
      doAccountCreation();
    }
  };

  private final AsyncCallback<ClientUserInformation> creationCallback =
      new AsyncCallback<ClientUserInformation>() {
        @Override public void onSuccess(ClientUserInformation result) {
          doAccountCreated(result);
        }

        @Override public void onFailure(Throwable caught) {
          doCreationFailed(caught);
        }
      };

  private final AccountServiceAsync accountService;

  private final Navigator navigator;
  private final AccountMessages messages;
  private final AlertController alerts;
  @Nullable private final View view;

  @Inject AccountCreationPagePresenter(
      AccountServiceAsync accountService,
      Navigator navigator,
      AccountMessages messages,
      AlertController alerts,
      View view,
      ClientPageConstants constants) {
    this.accountService = accountService;
    this.navigator = navigator;
    this.messages = messages;
    this.alerts = alerts;

    if (constants.getBoolean(AuthenticationConstant.IS_ADMIN)) {
      this.view = view;
    } else {
      this.view = null;
    }
  }

  private void doCreationFailed(Throwable caught) {
    alerts.newErrorAlertBuilder(messages.errorCreatingAccount())
        .create()
        .requestDisplay();
  }

  private void doAccountCreated(ClientUserInformation result) {
    alerts.newStatusAlertBuilder(messages.accountCreated(result.getDisplayName()))
        .create()
        .requestDisplay();
    navigator.navigateTo(new HomeApplicationPlace());
  }

  private void doAccountCreation() {
    String emailAddress = view.getEmailAddress();
    String displayName = view.getDisplayName();

    if (Strings.isNullOrEmpty(emailAddress)) {
      alerts.newErrorAlertBuilder(messages.mustSupplyEmailAddress())
          .create()
          .requestDisplay();
      return;
    }

    if (Strings.isNullOrEmpty(displayName)) {
      alerts.newErrorAlertBuilder(messages.mustSupplyDisplayName())
          .create()
          .requestDisplay();
      return;
    }

    accountService.createAccount(emailAddress, displayName, Role.MEMBER,
        alerts.newStatusAlertBuilder(messages.creatingAccount(displayName))
            .isFading(false)
            .create()
            .requestDisplay()
            .delegateTo(creationCallback));
  }

  @Override public Optional<? extends IsWidget> presentPageFor(
      AccountCreationApplicationPlace place) {
    if (view == null) {
      alerts.newErrorAlertBuilder(messages.accountCreationNotPermitted())
          .create()
          .requestDisplay();
    } else {
      view.getCreateControl().addClickHandler(createHandler);
    }

    return Optional.fromNullable(view);
  }
}
