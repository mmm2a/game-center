package com.morgan.client.game;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

/**
 * {@link Ginjector} for the Game application.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@GinModules(GameAppGinModule.class)
public interface GameAppGinjector extends Ginjector {

  /**
   * Retrieves the main game app class.
   */
  GameApp getGameApplication();
}
