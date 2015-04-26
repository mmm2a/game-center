package com.morgan.server.game.modules;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.morgan.shared.common.HasUniqueId;
import com.morgan.shared.game.modules.HasGameIdentifier;

/**
 * Interface representing an active, ongoing game.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface ActiveGame extends HasGameIdentifier, HasUniqueId {

  /**
   * Describes (summarizes) the state of this game.
   */
  SafeHtml summarize();

  /**
   * Retrieves the set of all participating players.
   */
  ImmutableSet<Long> getPlayers();

  /**
   * Retrieves the set of all players who could take a turn right now.
   */
  ImmutableSet<Long> getPlayersWhoCanTakeATurn();
}
