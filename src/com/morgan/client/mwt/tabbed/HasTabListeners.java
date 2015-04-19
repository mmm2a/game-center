package com.morgan.client.mwt.tabbed;

/**
 * Interface describing a type that can notify {@link TabListeners} of tab state changes.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface HasTabListeners {
  /**
   * Adds a new {@link TabListener} to the list of listeners to receive callbacks.
   */
  void addTabListener(TabListener tabListener);

  /**
   * Removes a previously registerd {@link TabListener} from the list of registered listeners.
   */
  void removeTabListener(TabListener tabListener);
}
