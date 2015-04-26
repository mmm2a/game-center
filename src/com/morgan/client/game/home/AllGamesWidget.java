package com.morgan.client.game.home;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.morgan.client.alert.Alert;
import com.morgan.client.alert.AlertController;
import com.morgan.client.alert.CommonAlertBindings.Loading;
import com.morgan.shared.game.GameServiceAsync;
import com.morgan.shared.game.modules.GameDescriptor;

/**
 * Widget for displaying the complete list of all games on the server.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class AllGamesWidget implements IsWidget {

  /**
   * View interface for the "all games" widget.
   *
   * @author mark@mark-morgan.net (Mark Morgan)
   */
  @ImplementedBy(ViewImpl.class)
  interface View extends IsWidget, HasWidgets.ForIsWidget {
  }

  /**
   * View interface for an individual game inside of the all games widget.
   *
   * @author mark@mark-morgan.net (Mark Morgan)
   */
  @ImplementedBy(DefaultGameWidget.class)
  interface GameWidget extends IsWidget, HasText, HasSafeHtml, HasSelectionHandlers<Widget> {
    /**
     * Sets the icon to display for this game.
     */
    void setIcon(SafeUri gameIcon);
  }

  private final AlertController alertController;
  private final HomeMessages homeMessages;

  private final View view;
  private final Provider<GameWidget> gameWidgetProvider;

  private final Function<GameDescriptor, GameWidget> gameDescriptorToWidgetFunction =
      new Function<GameDescriptor, AllGamesWidget.GameWidget>() {
        @Override public GameWidget apply(GameDescriptor input) {
          return createWidgetForGame(input);
        }
      };

  private final AsyncCallback<ImmutableSet<GameDescriptor>> gamesCallback =
      new AsyncCallback<ImmutableSet<GameDescriptor>>() {
        @Override public void onSuccess(ImmutableSet<GameDescriptor> result) {
          onGamesLoaded(result);
        }

        @Override public void onFailure(Throwable caught) {
          onGamesLoadingError(caught);
        }
      };

  @Inject AllGamesWidget(
      HomeResources resources,
      AlertController alertController,
      @Loading Alert loadingAlert,
      HomeMessages homeMessages,
      GameServiceAsync gameService,
      View view,
      Provider<GameWidget> gameWidgetProvider) {
    this.alertController = alertController;
    this.homeMessages = homeMessages;

    this.view = view;
    this.gameWidgetProvider = gameWidgetProvider;

    resources.css().ensureInjected();

    gameService.getAllGames(loadingAlert.requestDisplay().delegateTo(gamesCallback));
  }

  private GameWidget createWidgetForGame(GameDescriptor descriptor) {
    GameWidget widget = gameWidgetProvider.get();
    widget.setText(descriptor.getName());
    widget.setHTML(descriptor.getDescription());
    widget.setIcon(descriptor.getIcon());
    widget.addSelectionHandler(new GameSelectionHandler(descriptor));
    return widget;
  }

  private void onGamesLoaded(ImmutableSet<GameDescriptor> allGames) {
    view.clear();

    for (GameWidget widget : Iterables.transform(allGames, gameDescriptorToWidgetFunction)) {
      view.add(widget);
    }
  }

  private void onGamesLoadingError(Throwable cause) {
    alertController.newErrorAlertBuilder(homeMessages.errorLoadingAllGames())
        .isFading(false)
        .create()
        .requestDisplay();
  }

  @Override public Widget asWidget() {
    return view.asWidget();
  }

  /**
   * Default implementation of the {@link View} interface.
   */
  static class ViewImpl extends FlowPanel implements View {
    @Inject ViewImpl() {
    }
  }

  /**
   * Selection handler for games.
   *
   * @author mark@mark-morgan.net (Mark Morgan)
   */
  private class GameSelectionHandler implements SelectionHandler<Widget> {

    private final GameDescriptor gameDescriptor;

    GameSelectionHandler(GameDescriptor gameDescriptor) {
      this.gameDescriptor = Preconditions.checkNotNull(gameDescriptor);
    }

    @Override public void onSelection(SelectionEvent<Widget> event) {
      alertController.newStatusAlertBuilder("Game " + gameDescriptor.getName() + " selected")
          .isFading(true)
          .create()
          .requestDisplay();
    }
  }
}
