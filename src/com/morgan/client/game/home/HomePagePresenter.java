package com.morgan.client.game.home;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import com.morgan.client.page.PagePresenter;
import com.morgan.shared.game.home.HomeApplicationPlace;

/**
 * {@link PagePresenter} for the home page of the game application.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class HomePagePresenter implements PagePresenter<HomeApplicationPlace> {

  @Inject HomePagePresenter() {
  }

  @Override public Optional<? extends IsWidget> presentPageFor(HomeApplicationPlace place) {
    return Optional.of(new Label("Welcome to the game server!"));
  }
}
