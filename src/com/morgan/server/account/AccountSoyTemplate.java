package com.morgan.server.account;

import com.google.gwt.safehtml.shared.SafeUri;
import com.morgan.server.util.soy.Soy;
import com.morgan.server.util.soy.SoyParameter;
import com.morgan.server.util.soy.SoyTemplate;

/**
 * Soy template interface for the account package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Soy(namespace = "com.morgan.server.account",
    resource = "resources/account.soy")
interface AccountSoyTemplate extends SoyTemplate {

  String createdEmailHtml(
      @SoyParameter(name = "serverTitle") String serverTitle,
      @SoyParameter(name = "serverUrl") SafeUri serverUrl,
      @SoyParameter(name = "emailAddress") String emailAddress,
      @SoyParameter(name = "displayName") String displayName,
      @SoyParameter(name = "password") String password);

  String createdEmailPlain(
      @SoyParameter(name = "serverTitle") String serverTitle,
      @SoyParameter(name = "serverUrl") SafeUri serverUrl,
      @SoyParameter(name = "emailAddress") String emailAddress,
      @SoyParameter(name = "displayName") String displayName,
      @SoyParameter(name = "password") String password);
}
