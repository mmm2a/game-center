package com.morgan.client.nav;

import com.google.gwt.user.client.Window.Location;
import com.google.inject.Inject;

/**
 * Helper class (for testability) for the {@link Location} GWT class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class LocationHelper {

  @Inject LocationHelper() {
  }

  /**
   * See {@link Location#assign(String)}.
   */
  public void assign(String newUrl) {
    Location.assign(newUrl);
  }
}
