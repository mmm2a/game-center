package com.morgan.server.game.modules;

import java.util.Set;

import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * An interface that represents the connection between the server and a specific game module.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface GameModule {

  /**
   * Retrieves the short name for the game that this module represents.
   */
  String getName();

  /**
   * Retrieves the long description for the game that this module represents.
   */
  SafeHtml getDescription();

  /**
   * Gets the set of active games for this game module type for the active user.
   */
  Set<ActiveGame> getActiveGamesForCurrentUser();
}
