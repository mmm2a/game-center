package com.morgan.server.account;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.morgan.server.util.soy.SoyTemplateFactory;

/**
 * GUICE module for the account package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AccountModule extends AbstractModule {
  @Override protected void configure() {
    install(new AccountServletsModule());
  }

  @Provides @Singleton
  protected AccountSoyTemplate provideAccountSoyTemplate(SoyTemplateFactory factory) {
    return factory.createSoyTemplate(AccountSoyTemplate.class);
  }
}
