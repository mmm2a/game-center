package com.morgan.client.mwt.tabbed;

/**
 * Listener interface for a type that will receive callbacks when various tab events occur.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface TabListener {

  /** Callback method invoked when a tab is activated */
  void onTabActivated(Tab tab);

  /** Callback method invoked when a tab is deactivated */
  void onTabDeactivated(Tab tab);
}
