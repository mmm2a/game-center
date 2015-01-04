package com.morgan.server.email;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.inject.Provider;

/**
 * Abstract base class with common code for providing configured {@link Email} objects.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
abstract class AbstractEmailProvider<T extends Email> implements Provider<T> {

  private final EmailFlagAccessor flagAccessor;

  AbstractEmailProvider(EmailFlagAccessor flagAccessor) {
    this.flagAccessor = flagAccessor;
  }

  protected abstract T createInstance();

  @Override public T get() {
    T email = createInstance();

    email.setHostName(flagAccessor.smtpHostname());
    email.setSmtpPort(flagAccessor.smtpPort());

    String username = flagAccessor.smtpUserName();
    if (!Strings.isNullOrEmpty(username)) {
      String password = flagAccessor.smtpPassword();
      Preconditions.checkState(!Strings.isNullOrEmpty(password));
      email.setAuthenticator(new DefaultAuthenticator(username, password));
    }
    email.setSSLOnConnect(flagAccessor.isSslOnConnect());
    try {
      email.setFrom(flagAccessor.fromAddress());
    } catch (EmailException e) {
      Throwables.propagate(e);
    }

    return email;
  }
}
