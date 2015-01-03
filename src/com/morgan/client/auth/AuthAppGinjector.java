package com.morgan.client.auth;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

/**
 * {@link Ginjector} for the Auth application.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@GinModules(AuthAppGinModule.class)
public interface AuthAppGinjector extends Ginjector {

  /**
   * Retrieves the main auth app class.
   */
  AuthApp getAuthApplication();
}
