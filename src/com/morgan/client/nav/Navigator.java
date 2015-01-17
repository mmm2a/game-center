package com.morgan.client.nav;

import com.morgan.shared.nav.ApplicationPlace;

/**
 * An interface for a type that can be used to navigate to a specific place (including
 * {@link ApplicationPlace} instances.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface Navigator {

  /** Navigate to the {@link ApplicationPlace} indicated. */
  void navigateTo(ApplicationPlace place);

  /** Navigate back in the browser history */
  void back();

  /** Navigate forward in the browser history */
  void forward();
}
