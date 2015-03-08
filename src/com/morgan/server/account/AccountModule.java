package com.morgan.server.account;

import com.google.inject.AbstractModule;

/**
 * GUICE module for the account package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AccountModule extends AbstractModule {
  @Override protected void configure() {
    install(new AccountServletsModule());
  }
}
