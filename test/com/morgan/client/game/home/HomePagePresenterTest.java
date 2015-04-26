package com.morgan.client.game.home;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;
import com.morgan.client.mwt.tabbed.Orientation;
import com.morgan.client.mwt.tabbed.Tab;
import com.morgan.client.mwt.tabbed.TabbedPanel;
import com.morgan.testing.FakeMessagesFactory;

/**
 * Tests for the {@link HomePagePresenter} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class HomePagePresenterTest {

  private static final HomeMessages MESSAGES = FakeMessagesFactory.create(HomeMessages.class);

  @Mock TabbedPanel mockTabbedPanel;
  @Mock AllGamesWidget mockAllGamesWidget;

  @Mock private Tab mockActiveTab;
  @Mock private Tab mockAllTab;
  @Mock private Tab mockIOwnTab;

  @Mock private IsWidget mockIsWidgetForActiveGames;
  @Mock private IsWidget mockIsWidgetForOwnedGames;

  private HomePagePresenter presenter;
  private HomePagePresenter spyPresenter;

  @Before public void createTestInstances() {
    presenter = new HomePagePresenter(MESSAGES, mockTabbedPanel, mockAllGamesWidget);
    spyPresenter = spy(presenter);

    doReturn(mockIsWidgetForActiveGames)
        .when(spyPresenter)
        .createPlaceHolderWidget(MESSAGES.activeGames());
    doReturn(mockIsWidgetForOwnedGames)
        .when(spyPresenter)
        .createPlaceHolderWidget(MESSAGES.gamesICreated());
  }

  @Before public void setUpCommonMockInteractions() {
    when(mockTabbedPanel.createAndAddTab(
        eq(HomePagePresenter.Tab.ACTIVE_GAMES),
        any(SafeHtml.class)))
        .thenReturn(mockActiveTab);
    when(mockTabbedPanel.createAndAddTab(
        eq(HomePagePresenter.Tab.GAME_PORTALS),
        any(SafeHtml.class)))
        .thenReturn(mockAllTab);
    when(mockTabbedPanel.createAndAddTab(
        eq(HomePagePresenter.Tab.GAMES_I_OWN),
        any(SafeHtml.class)))
        .thenReturn(mockIOwnTab);
  }

  @Test public void presentPageFor_constructsTabs() {
    assertThat(spyPresenter.presentPageFor(null)).hasValue(mockTabbedPanel);

    verify(mockTabbedPanel).setOrientation(Orientation.VERTICAL);

    InOrder order = inOrder(mockTabbedPanel);
    order.verify(mockTabbedPanel)
        .createAndAddTab(HomePagePresenter.Tab.ACTIVE_GAMES, MESSAGES.activeGames());
    order.verify(mockTabbedPanel)
        .createAndAddTab(HomePagePresenter.Tab.GAME_PORTALS, MESSAGES.allGames());
    order.verify(mockTabbedPanel)
        .createAndAddTab(HomePagePresenter.Tab.GAMES_I_OWN, MESSAGES.gamesICreated());

    verify(mockActiveTab).setWidget(mockIsWidgetForActiveGames);
    verify(mockAllTab).setWidget(mockAllGamesWidget);
    verify(mockIOwnTab).setWidget(mockIsWidgetForOwnedGames);
  }
}
