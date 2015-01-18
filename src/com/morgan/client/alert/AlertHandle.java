package com.morgan.client.alert;

/**
 * Represents an alert that has been displayed (though it may have since been cancelled already).
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface AlertHandle {

  /**
   * Cancels the display of this alert (has no effect if the alert has already been cancelled).
   */
  void cancel();
}
