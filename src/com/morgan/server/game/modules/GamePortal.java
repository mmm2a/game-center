package com.morgan.server.game.modules;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.Service;
import com.google.gwt.safehtml.shared.SafeUri;

/**
 * Interface for the portal between the server and a specific game.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface GamePortal extends Service {
  /**
   * Gets the optional icon to display for this game. Games that don't define their own icon will
   * use the default game icon.
   */
  Optional<SafeUri> getGameIcon();
}
