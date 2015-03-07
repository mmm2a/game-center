package com.morgan.client.nav;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.morgan.client.page.PagePresenterHelper;
import com.morgan.shared.nav.ApplicationPlace;
import com.morgan.shared.nav.ClientApplication;

/**
 * Tests for the {@link DefaultNavigation} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultNavigationTest {

  private static final String INITIAL_TOKEN = "initial token";

  @Mock private Scheduler mockScheduler;

  @Mock private ApplicationPlace mockDefaultPlace;
  @Mock private ApplicationPlace mockCurrentPlace;
  @Mock private ApplicationPlace mockOtherPlace;

  @Mock private HistoryHelper mockHistoryHelper;
  @Mock private PlaceRepresentationHelper mockPlaceRepresentationHelper;
  @Mock private PagePresenterHelper mockPagePresenterHelper;
  @Mock private LocationHelper mockLocationHelper;
  @Mock private UrlCreator mockUrlCreator;

  @Captor private ArgumentCaptor<ValueChangeHandler<String>> valueChangeHandlerCaptor;
  @Captor private ArgumentCaptor<ScheduledCommand> scheduledCommandCaptor;

  private DefaultNavigation navigation;

  @Before public void createTestInstances() {
    when(mockHistoryHelper.getToken()).thenReturn(INITIAL_TOKEN);

    navigation = new DefaultNavigation(
        mockScheduler,
        mockLocationHelper,
        mockDefaultPlace,
        mockHistoryHelper,
        mockPlaceRepresentationHelper,
        mockUrlCreator,
        mockPagePresenterHelper);
  }

  @Test public void construction_addsChangeHandler() {
    verify(mockHistoryHelper).addValueChangeHandler(valueChangeHandlerCaptor.capture());
  }

  @Test public void construction_callsOnPathChangedDeferred() {
    verify(mockScheduler).scheduleDeferred(scheduledCommandCaptor.capture());
    scheduledCommandCaptor.getValue().execute();
    verify(mockPlaceRepresentationHelper).parseFromHistoryToken(INITIAL_TOKEN);
  }

  @Test public void onPathChanged_unrecognizedPlace_replacesWithDefault() {
    verify(mockHistoryHelper).addValueChangeHandler(valueChangeHandlerCaptor.capture());
    reset(mockPlaceRepresentationHelper, mockPagePresenterHelper ,mockHistoryHelper);
    when(mockPlaceRepresentationHelper.representPlaceAsHistoryToken(mockDefaultPlace))
        .thenReturn("default place");

    valueChangeHandlerCaptor.getValue()
        .onValueChange(new ValueChangeEvent<String>("unrecognized") {});
    verify(mockHistoryHelper).replaceItem("default place", true);
  }

  @Test public void onPathChanged_recognizedPlace_presentsPlacesPage() {
    String historyToken = "history token";
    verify(mockHistoryHelper).addValueChangeHandler(valueChangeHandlerCaptor.capture());
    reset(mockPlaceRepresentationHelper, mockPagePresenterHelper ,mockHistoryHelper);
    when(mockPlaceRepresentationHelper.parseFromHistoryToken(historyToken))
        .thenReturn(mockOtherPlace);

    valueChangeHandlerCaptor.getValue()
        .onValueChange(new ValueChangeEvent<String>(historyToken) {});
    verify(mockPagePresenterHelper).presentPageFor(mockOtherPlace);
  }

  @Test public void getCurrentPlace() {
    String historyToken = "history token";
    verify(mockHistoryHelper).addValueChangeHandler(valueChangeHandlerCaptor.capture());
    reset(mockPlaceRepresentationHelper, mockPagePresenterHelper ,mockHistoryHelper);
    when(mockPlaceRepresentationHelper.parseFromHistoryToken(historyToken))
        .thenReturn(mockOtherPlace);

    valueChangeHandlerCaptor.getValue()
        .onValueChange(new ValueChangeEvent<String>(historyToken) {});

    assertThat(navigation.getCurrentPlace()).isEqualTo(mockOtherPlace);
  }

  @Test public void back_callsThroughToHistoryHelper() {
    navigation.back();
    verify(mockHistoryHelper).back();
  }

  @Test public void forward_callsThroughToHistoryHelper() {
    navigation.forward();
    verify(mockHistoryHelper).forward();
  }

  @Test public void navigateTo_withinSameGwtApp() {
    when(mockPlaceRepresentationHelper.representPlaceAsHistoryToken(mockOtherPlace))
        .thenReturn("new token");
    navigation.currentPlace = mockCurrentPlace;

    when(mockCurrentPlace.getClientApplication()).thenReturn(ClientApplication.GAME_SERVER);
    when(mockOtherPlace.getClientApplication()).thenReturn(ClientApplication.GAME_SERVER);

    navigation.navigateTo(mockOtherPlace);
    verify(mockHistoryHelper).newItem("new token", true);
  }

  @Test public void navigateTo_inDifferentGwtApp() {
    navigation.currentPlace = mockCurrentPlace;

    when(mockCurrentPlace.getClientApplication()).thenReturn(ClientApplication.AUTHENTICATION);
    when(mockOtherPlace.getClientApplication()).thenReturn(ClientApplication.GAME_SERVER);

    when(mockUrlCreator.createUrlFor(mockOtherPlace)).thenReturn("new url");

    navigation.navigateTo(mockOtherPlace);
    verify(mockLocationHelper).assign("new url");
  }
}
