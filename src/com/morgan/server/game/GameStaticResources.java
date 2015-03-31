package com.morgan.server.game;

import com.google.gwt.safehtml.shared.SafeUri;
import com.morgan.server.staticres.StaticResource;
import com.morgan.server.staticres.StaticResources;

/**
 * Static resources for the game package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
interface GameStaticResources extends StaticResources {

  @StaticResource("resources/default-game-icon.png")
  SafeUri defaultGameIcon();
}
