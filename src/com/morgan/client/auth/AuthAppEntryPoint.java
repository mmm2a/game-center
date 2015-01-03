package com.morgan.client.auth;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Entry point for the authentication web app.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AuthAppEntryPoint implements EntryPoint {

  private final AuthAppGinjector injector = GWT.create(AuthAppGinjector.class);

  @Override public void onModuleLoad() {
    injector.getAuthApplication().startApplication();
  }
}
