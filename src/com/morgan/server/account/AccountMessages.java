package com.morgan.server.account;

import com.google.gwt.i18n.client.Messages;
import com.google.inject.Inject;

/**
 * {@link Messages} interface for the account package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class AccountMessages implements Messages {

  @Inject AccountMessages() {
  }

  /*
  @DefaultMessage("New account for {0} created on {1}")
  @Description(
      "Subject for an email sent to a user confirming the creation of an account for him/her")
  */
  String newAccountSubject(String emailAddress, String serverTitle) {
    return String.format("New account for %s created on %s", emailAddress, serverTitle);
  }
}
