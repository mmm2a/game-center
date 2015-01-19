package com.morgan.client.alert;

import com.google.gwt.resources.client.CssResource;

/**
 * CSS resource interface for the alerts package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
interface AlertCss extends CssResource {
  int FADE_DURATION_MS();

  String alertsContainer();
  String alerts();
  String alert();
  String status();
  String error();
  String fade();
}
