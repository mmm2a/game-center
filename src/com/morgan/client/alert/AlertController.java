package com.morgan.client.alert;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

/**
 * Controller for managing alerts in the UI.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@ImplementedBy(DefaultAlertController.class)
public interface AlertController {
  /**
   * Creates a new alert builder for creating a status alert.
   */
  AlertBuilder newStatusAlertBuilder(String text);

  /**
   * Creates a new alert builder for creating an error alert.
   */
  AlertBuilder newErrorAlertBuilder(String text);

  /**
   * Creates a new alert builder for creating a status alert.
   */
  AlertBuilder newStatusAlertBuilder(SafeHtml contents);

  /**
   * Creates a new alert builder for creating an error alert.
   */
  AlertBuilder newErrorAlertBuilder(SafeHtml contents);

  /**
   * Creates a new alert builder for creating a status alert.
   */
  AlertBuilder newStatusAlertBuilder(IsWidget contents);

  /**
   * Creates a new alert builder for creating an error alert.
   */
  AlertBuilder newErrorAlertBuilder(IsWidget contents);
}
