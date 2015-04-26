package com.morgan.client.game.home;

import static com.google.common.truth.Truth.assertAbout;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.morgan.client.game.home.AllGamesWidget.GameWidget;
import com.morgan.shared.game.GameServiceAsync;
import com.morgan.shared.game.modules.GameDescriptor;
import com.morgan.shared.game.modules.GameIdentifier;
import com.morgan.testing.FakeAlertController;
import com.morgan.testing.FakeAlertController.FakeAlert;
import com.morgan.testing.FakeCssFactory;
import com.morgan.testing.FakeMessagesFactory;
import com.morgan.testing.MoreProviders;

/**
 * Tests for the {@link AllGamesWidget} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class AllGamesWidgetTests {

  private static final GameDescriptor DESCRIPTOR_1 = getDescriptorFor(1);
  private static final GameDescriptor DESCRIPTOR_2 = getDescriptorFor(2);
  private static final GameDescriptor DESCRIPTOR_3 = getDescriptorFor(3);

  private static final ImmutableSet<GameDescriptor> ALL_DESCRIPTORS = ImmutableSet.of(
      DESCRIPTOR_1, DESCRIPTOR_2, DESCRIPTOR_3);

  private static final HomeMessages MESSAGES = FakeMessagesFactory.create(HomeMessages.class);
  private static final HomeCss CSS = FakeCssFactory.forResource(HomeCss.class).createProxy();
  private static final HomeResources RESOURCES = new HomeResources() {
    @Override public HomeCss css() {
      return CSS;
    }
  };

  @Mock private AllGamesWidget.View mockView;

  @Mock private AllGamesWidget.GameWidget mockGameWidget1;
  @Mock private AllGamesWidget.GameWidget mockGameWidget2;
  @Mock private AllGamesWidget.GameWidget mockGameWidget3;

  @Mock private GameServiceAsync mockGameService;

  @Captor private ArgumentCaptor<AsyncCallback<ImmutableSet<GameDescriptor>>> callbackCaptor;
  @Captor private ArgumentCaptor<SelectionHandler<Widget>> selectionHandlerCaptor;

  private FakeAlertController alertController;
  private FakeAlert loadingAlert;

  @Before public void createTestInstances() {
    alertController = new FakeAlertController();
    loadingAlert = alertController.newStatusAlertBuilder("Loading").create();

    new AllGamesWidget(
        RESOURCES,
        alertController,
        loadingAlert,
        MESSAGES,
        mockGameService,
        mockView,
        MoreProviders.of(mockGameWidget1, mockGameWidget2, mockGameWidget3));
  }

  @Test public void construction_callsService() {
    verify(mockGameService).getAllGames(Mockito.<AsyncCallback<ImmutableSet<GameDescriptor>>>any());
  }

  @Test public void construction_showsLoadingAlert() {
    assertAbout(FakeAlertController.ALERT).that(loadingAlert).isDisplayed();
  }

  @Test public void onGamesError_clearsLoadingAlert() {
    verify(mockGameService).getAllGames(callbackCaptor.capture());
    callbackCaptor.getValue().onFailure(new NullPointerException());
    assertAbout(FakeAlertController.ALERT).that(loadingAlert).isNotDisplayed();
  }

  @Test public void onGamesError_showsErrorAlert() {
    verify(mockGameService).getAllGames(callbackCaptor.capture());
    callbackCaptor.getValue().onFailure(new NullPointerException());

    FakeAlert errorAlert = alertController.newErrorAlertBuilder(MESSAGES.errorLoadingAllGames())
        .isFading(false)
        .create();
    assertAbout(FakeAlertController.ALERT).that(errorAlert).isDisplayed();
  }

  @Test public void onGamesResult_clearsLoadingAlert() {
    verify(mockGameService).getAllGames(callbackCaptor.capture());
    callbackCaptor.getValue().onSuccess(ALL_DESCRIPTORS);
    assertAbout(FakeAlertController.ALERT).that(loadingAlert).isNotDisplayed();
  }

  private void verifyGameSelected(GameDescriptor desc) {
    FakeAlert alert = alertController.newStatusAlertBuilder("Game " + desc.getName() + " selected")
        .isFading(true)
        .create();
    assertAbout(FakeAlertController.ALERT).that(alert).isDisplayed();
  }

  private void verifyGameCreated(
      GameWidget expectedWidget,
      GameDescriptor desc) {
    verify(expectedWidget).setText(desc.getName());
    verify(expectedWidget).setHTML(desc.getDescription());
    verify(expectedWidget).setIcon(desc.getIcon());

    verify(expectedWidget).addSelectionHandler(selectionHandlerCaptor.capture());
    selectionHandlerCaptor.getValue().onSelection(null);
    verifyGameSelected(desc);
  }

  @Test public void onGamesResult_createsGames() {
    verify(mockGameService).getAllGames(callbackCaptor.capture());
    callbackCaptor.getValue().onSuccess(ALL_DESCRIPTORS);

    verifyGameCreated(mockGameWidget1, DESCRIPTOR_1);
    verifyGameCreated(mockGameWidget2, DESCRIPTOR_2);
    verifyGameCreated(mockGameWidget3, DESCRIPTOR_3);
  }

  @Test public void onGamesResult_addsGameWidgets() {
    verify(mockGameService).getAllGames(callbackCaptor.capture());
    callbackCaptor.getValue().onSuccess(ALL_DESCRIPTORS);

    verify(mockView).add(mockGameWidget1);
    verify(mockView).add(mockGameWidget2);
    verify(mockView).add(mockGameWidget3);
  }

  private static GameDescriptor getDescriptorFor(int number) {
    GameIdentifier id = new GameIdentifier("id-" + number);
    return GameDescriptor.builderFor(id)
        .setName(String.format("Game %d", number))
        .setDescription(SafeHtmlUtils.fromString(String.format("Game description %d", number)))
        .setIcon(UriUtils.fromString(String.format("icon-%d", number)))
        .build();
  }
}
