package com.morgan.client.auth;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * Messages interface for the authentication package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
interface AuthenticationMessages extends Messages {
  @DefaultMessage("Logging in")
  @Description("Status message shown to a user when s/he tries to log in and we are contacting the "
      + "server to perform the log in action")
  SafeHtml loggingInStatus();

  @DefaultMessage("Logging out")
  @Description("Status message shown to a user when s/he tries to log out and we are contacting "
      + "the server to perform the log out action")
  SafeHtml loggingOutStatus();

  @DefaultMessage("Unable to log in")
  @Description("Error message shown to a user when s/he tries to log in and fails to do so")
  SafeHtml logInFailed();

  @DefaultMessage("There was an error while trying to log in.")
  @Description(
      "Error message shown to a user when s/he tries to log in and the call to the server fails")
  SafeHtml logInError();

  @DefaultMessage("There was an error while trying to log out.")
  @Description("Error message shown to a user whe s/he tries to log out and there is an error")
  SafeHtml logOutFailed();
}
