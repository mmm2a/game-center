package com.morgan.client.account;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * {@link Messages} interface for the account page.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
interface AccountMessages extends Messages {

  @DefaultMessage("Not permitted to create accounts")
  @Description("Error message shown to a user when s/he tries to navigate to a page to create "
      + "accounts and is not allowed to do so")
  SafeHtml accountCreationNotPermitted();

  @DefaultMessage("An email address must be specified for account creation")
  @Description("Error shown to a user when s/he tries to create a new account without an email "
      + "address specified")
  SafeHtml mustSupplyEmailAddress();

  @DefaultMessage("An email address must be specified for account creation")
  @Description("Error shown to a user when s/he tries to create a new account without an email "
      + "address specified")
  SafeHtml mustSupplyDisplayName();

  @DefaultMessage("Creating an account for <b>{0}</b>")
  @Description(
      "Status message shown to a user when they are in the process of having an account created")
  SafeHtml creatingAccount(String displayName);

  @DefaultMessage("Unable to create new account")
  @Description("Error message shown to a user when they fail to create a user account")
  SafeHtml errorCreatingAccount();

  @DefaultMessage("Account created for <b>{0}</b>")
  @Description("A status message shown to a user when they have successfully created a new account")
  SafeHtml accountCreated(String displayName);
}
