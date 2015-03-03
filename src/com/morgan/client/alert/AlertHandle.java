package com.morgan.client.alert;

import com.google.gwt.user.client.rpc.AsyncCallback;

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

  /**
   * Delegates this alert handle to the given callback.  When the callback comes in (whether it
   * succeeds or fails) the alert will be cancelled, and then the delegate callback will be called.
   */
  <T> AsyncCallback<T> delegateTo(AsyncCallback<T> delegate);
}
