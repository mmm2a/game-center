package com.morgan.server.email;

import org.apache.commons.mail.HtmlEmail;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * {@link Provider} for html emails.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class HtmlEmailProvider
    extends AbstractEmailProvider<HtmlEmail> implements Provider<HtmlEmail> {

  @Inject HtmlEmailProvider(EmailFlagAccessor flagAccessor) {
    super(flagAccessor);
  }

  @Override protected HtmlEmail createInstance() {
    return new HtmlEmail();
  }
}
