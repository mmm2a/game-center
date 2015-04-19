package com.morgan.client.game.home;

import com.google.common.base.Optional;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
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

  private final TabbedPanel panel;

  @Inject HomePagePresenter(TabbedPanel panel) {
    this.panel = panel;
  }

  @Override public Optional<? extends IsWidget> presentPageFor(HomeApplicationPlace place) {
    panel.asWidget().setWidth("100%");
    panel.asWidget().setHeight("100%");
    panel.setOrientation(Orientation.VERTICAL);
    panel.createAndAddTab("Active games", SafeHtmlUtils.fromString("Active games"))
        .setWidget(new Label("Active games"));
    panel.createAndAddTab("Game portals", SafeHtmlUtils.fromString("Game portals"))
        .setWidget(new Label("Game portals"));
    panel.createAndAddTab("Games I own", SafeHtmlUtils.fromString("Games I own"))
        .setWidget(new Label("Games I own"));

    return Optional.of(panel);
  }
}
