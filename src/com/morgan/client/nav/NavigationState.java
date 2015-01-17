package com.morgan.client.nav;

import com.morgan.shared.nav.ApplicationPlace;

/**
 * An interface for a type that can be used to get the current state of the navigation
 * sub-system.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface NavigationState {

  /** Gets the target location of the current page */
  ApplicationPlace getCurrentPlace();
}
