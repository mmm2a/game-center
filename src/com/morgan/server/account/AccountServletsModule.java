package com.morgan.server.account;

import com.google.inject.servlet.ServletModule;
import com.morgan.shared.account.AccountService;

/**
 * Servlets GUICE module for the account package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class AccountServletsModule extends ServletModule {

  @Override protected void configureServlets() {
    bind(AccountService.class).to(DefaultAccountService.class);

    serve("/services/account-service").with(DefaultAccountService.class);
  }
}
