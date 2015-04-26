package com.morgan.client.alert;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * Messages interface for the alerts package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
interface AlertMessages extends Messages {
  @DefaultMessage("Loading")
  @Description("Status alert shown to a user to indicate that content is being loaded")
  SafeHtml loadingAlert();
}
