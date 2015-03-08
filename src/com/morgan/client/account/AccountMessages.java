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
}
