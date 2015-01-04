package com.morgan.server.email;

import org.apache.commons.mail.SimpleEmail;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * {@link Provider} for plain text emails.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class PlainTextEmailProvider
    extends AbstractEmailProvider<SimpleEmail> implements Provider<SimpleEmail> {

  @Inject PlainTextEmailProvider(EmailFlagAccessor flagAccessor) {
    super(flagAccessor);
  }

  @Override protected SimpleEmail createInstance() {
    return new SimpleEmail();
  }
}
