package com.morgan.client.game.home;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import com.morgan.client.mwt.tabbed.Orientation;
import com.morgan.client.mwt.tabbed.TabbedPanel;
import com.morgan.client.page.PagePresenter;
import com.morgan.shared.game.home.HomeApplicationPlace;

/**
 * {@link PagePresenter} for the home page of the game application.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class HomePagePresenter implements PagePresenter<HomeApplicationPlace> {

  /**
   * Enumeration of all possible tabs in the tabbed panel.
   *
   * @author mark@mark-morgan.net (Mark Morgan)
   */
  enum Tab {
    ACTIVE_GAMES,
    GAME_PORTALS,
    GAMES_I_OWN;
  }

  private final HomeMessages messages;

  private final TabbedPanel panel;
  private final AllGamesWidget allGames;

  @Inject HomePagePresenter(
      HomeMessages messages,
      TabbedPanel panel,
      AllGamesWidget allGames) {
    this.messages = messages;
    this.panel = panel;
    this.allGames = allGames;
  }

  @VisibleForTesting IsWidget createPlaceHolderWidget(SafeHtml title) {
    return new Label(title.asString());
  }

  @Override public Optional<? extends IsWidget> presentPageFor(HomeApplicationPlace place) {
    panel.setOrientation(Orientation.VERTICAL);
    panel.createAndAddTab(Tab.ACTIVE_GAMES, messages.activeGames())
        .setWidget(createPlaceHolderWidget(messages.activeGames()));
    panel.createAndAddTab(Tab.GAME_PORTALS, messages.allGames())
        .setWidget(allGames);
    panel.createAndAddTab(Tab.GAMES_I_OWN, messages.gamesICreated())
        .setWidget(createPlaceHolderWidget(messages.gamesICreated()));

    return Optional.of(panel);
  }
}
