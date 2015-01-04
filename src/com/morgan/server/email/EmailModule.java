package com.morgan.server.email;

import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.morgan.server.util.flag.FlagAccessorFactory;

/**
 * GUICE module for configuring the email package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class EmailModule extends AbstractModule {

  @Override protected void configure() {
    bind(SimpleEmail.class).toProvider(PlainTextEmailProvider.class);
    bind(HtmlEmail.class).toProvider(HtmlEmailProvider.class);
  }

  @Provides @Singleton
  protected EmailFlagAccessor provideEmailFlagAccessor(FlagAccessorFactory factory) {
    return factory.getFlagAccessor(EmailFlagAccessor.class);
  }
}
