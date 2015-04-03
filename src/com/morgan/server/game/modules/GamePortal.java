package com.morgan.server.game.modules;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.Service;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.morgan.shared.game.modules.HasGameIdentifier;

/**
 * Interface for the portal between the server and a specific game.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface GamePortal extends Service, HasGameIdentifier {

  /**
   * Retrieves the short name for this game (the title).
   */
  String getName();

  /**
   * Gets a longer description (a paragraph) that describes this game.
   */
  SafeHtml getDescription();


  /**
   * Gets the optional icon to display for this game. Games that don't define their own icon will
   * use the default game icon.
   */
  Optional<SafeUri> getGameIcon();
}
