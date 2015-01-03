package com.morgan.shared.auth;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.morgan.shared.common.BackendException;

/**
 * Service interface for the authentication service used by the game.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RemoteServiceRelativePath("../services/authentication-service")
public interface AuthenticationService extends RemoteService {

  /**
   * Method invoked when a user wants to authenticate his/herself against the server.
   */
  boolean authenticate(String emailAddress, String password) throws BackendException;
}
