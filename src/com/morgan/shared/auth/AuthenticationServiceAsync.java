package com.morgan.shared.auth;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous version of the {@link AuthenticationService}.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface AuthenticationServiceAsync {

  /** See {@link AuthenticationService#authenticate(String, char[])} */
  void authenticate(String emailAddress, String password, AsyncCallback<Boolean> callback);
}
