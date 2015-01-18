package com.morgan.client.alert;

/**
 * A representation of an alert that can be displayed.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface Alert {
  /**
   * Requests that this alert be displayed on the screen.  If the alert is already being displayed,
   * this request merely creates another handle to the currently displayed alert.
   */
  AlertHandle requestDisplay();
}
