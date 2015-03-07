package com.morgan.client.account;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import com.morgan.client.page.PagePresenter;
import com.morgan.shared.account.AccountCreationApplicationPlace;

/**
 * {@link PagePresenter} for account creation.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class AccountCreationPagePresenter
    implements PagePresenter<AccountCreationApplicationPlace> {

  @Inject AccountCreationPagePresenter() {
  }

  @Override public IsWidget presentPageFor(AccountCreationApplicationPlace place) {
    return new Label("Welcome to the account creation page!");
  }
}
