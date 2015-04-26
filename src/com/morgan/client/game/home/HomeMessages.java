package com.morgan.client.game.home;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * Messages for the home package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
interface HomeMessages extends Messages {

  @DefaultMessage("Unable to load games")
  @Description(
      "Error message indicating to the user that there was an error while trying to load all games")
  SafeHtml errorLoadingAllGames();

  @DefaultMessage("In progress")
  @Description("Label for a tab in a tabbed pane that indicates its for games that are in progress")
  SafeHtml activeGames();

  @DefaultMessage("All games")
  @Description("Label for a tab in a tabbed pane that indicates its for listing all games")
  SafeHtml allGames();

  @DefaultMessage("Created by me")
  @Description("Label for a tab in a tabbed pane that indicates its for games that were created by "
      + "the current user")
  SafeHtml gamesICreated();
}
