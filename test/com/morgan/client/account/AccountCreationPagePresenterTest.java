package com.morgan.client.account;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.morgan.client.nav.Navigator;
import com.morgan.shared.account.AccountServiceAsync;
import com.morgan.shared.auth.AuthenticationConstant;
import com.morgan.shared.auth.ClientUserInformation;
import com.morgan.shared.common.Role;
import com.morgan.shared.game.home.HomeApplicationPlace;
import com.morgan.testing.FakeAlertController;
import com.morgan.testing.FakeClientPageConstants;
import com.morgan.testing.FakeMessagesFactory;

/**
 * Tests for the {@link AccountCreationPagePresenter} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountCreationPagePresenterTest {

  private static final AccountMessages MESSAGES = FakeMessagesFactory.create(AccountMessages.class);

  private static final String EMAIL = "email address";
  private static final String DISPLAY = "display name";

  @Mock private AccountServiceAsync mockService;
  @Mock private Navigator mockNavigator;
  @Mock private AccountCreationPagePresenter.View mockView;

  @Mock private HasClickHandlers mockCreateControl;

  @Captor private ArgumentCaptor<ClickHandler> clickHandlerCaptor;
  @Captor private ArgumentCaptor<AsyncCallback<ClientUserInformation>> callbackCaptor;

  private FakeAlertController alerts;
  private FakeClientPageConstants constants;

  @Before public void createTestInstances() {
    alerts = new FakeAlertController();
    constants = new FakeClientPageConstants();
  }

  @Before public void setUpCommonMockInteractions() {
    when(mockView.getCreateControl()).thenReturn(mockCreateControl);
    when(mockView.getEmailAddress()).thenReturn(EMAIL);
    when(mockView.getDisplayName()).thenReturn(DISPLAY);
  }

  private AccountCreationPagePresenter createPresenter(boolean isAdmin) {
    constants.setValue(AuthenticationConstant.IS_ADMIN, Boolean.toString(isAdmin));
    return new AccountCreationPagePresenter(
        mockService, mockNavigator, MESSAGES, alerts, mockView, constants);
  }

  @Test public void presentPageFor_notAdmin_showsError() {
    assertThat(createPresenter(false).presentPageFor(null)).isAbsent();
    assertAbout(FakeAlertController.ALERT)
        .that(alerts.newErrorAlertBuilder(MESSAGES.accountCreationNotPermitted()).create())
        .isDisplayed();
    verifyZeroInteractions(mockService, mockNavigator);
  }

  @Test public void presentPageFor_isAdmin_showsView() {
    assertThat(createPresenter(true).presentPageFor(null)).hasValue(mockView);
    assertAbout(FakeAlertController.ALERT)
        .that(alerts.newErrorAlertBuilder(MESSAGES.accountCreationNotPermitted()).create())
        .isNotDisplayed();
  }

  private ClickHandler verifyClickHandlerRegistered() {
    verify(mockCreateControl).addClickHandler(clickHandlerCaptor.capture());
    return clickHandlerCaptor.getValue();
  }

  private AsyncCallback<ClientUserInformation> verifyServiceCalled() {
    verify(mockService)
        .createAccount(eq(EMAIL), eq(DISPLAY), eq(Role.MEMBER), callbackCaptor.capture());
    return callbackCaptor.getValue();
  }

  @Test public void onClick_noEmail_showsError() {
    when(mockView.getEmailAddress()).thenReturn("");

    createPresenter(true).presentPageFor(null);
    verifyClickHandlerRegistered().onClick(null);

    assertAbout(FakeAlertController.ALERT)
        .that(alerts.newErrorAlertBuilder(MESSAGES.mustSupplyEmailAddress())
            .create())
        .isDisplayed();
    verifyZeroInteractions(mockService);
  }

  @Test public void onClick_noDisplay_showsError() {
    when(mockView.getDisplayName()).thenReturn("");

    createPresenter(true).presentPageFor(null);
    verifyClickHandlerRegistered().onClick(null);

    assertAbout(FakeAlertController.ALERT)
        .that(alerts.newErrorAlertBuilder(MESSAGES.mustSupplyDisplayName())
            .create())
        .isDisplayed();
    verifyZeroInteractions(mockService);
  }

  @Test public void onClick_callsToService() {
    createPresenter(true).presentPageFor(null);
    verifyClickHandlerRegistered().onClick(null);
    verifyServiceCalled();
    assertAbout(FakeAlertController.ALERT)
        .that(alerts.newStatusAlertBuilder(MESSAGES.creatingAccount(DISPLAY))
            .isFading(false)
            .create())
        .isDisplayed();
  }

  @Test public void onServiceFailure_displaysError() {
    createPresenter(true).presentPageFor(null);
    verifyClickHandlerRegistered().onClick(null);
    verifyServiceCalled().onFailure(new NullPointerException());
    assertAbout(FakeAlertController.ALERT)
        .that(alerts.newStatusAlertBuilder(MESSAGES.creatingAccount(DISPLAY))
            .isFading(false)
            .create())
        .isNotDisplayed();
    assertAbout(FakeAlertController.ALERT)
        .that(alerts.newErrorAlertBuilder(MESSAGES.errorCreatingAccount())
            .create())
        .isDisplayed();
    verifyZeroInteractions(mockNavigator);
  }

  @Test public void onServiceFailure_navigates() {
    createPresenter(true).presentPageFor(null);
    verifyClickHandlerRegistered().onClick(null);
    verifyServiceCalled()
        .onSuccess(ClientUserInformation.withPrivlidgedInformation(DISPLAY, Role.MEMBER));
    assertAbout(FakeAlertController.ALERT)
        .that(alerts.newStatusAlertBuilder(MESSAGES.creatingAccount(DISPLAY))
            .isFading(false)
            .create())
        .isNotDisplayed();
    assertAbout(FakeAlertController.ALERT)
        .that(alerts.newStatusAlertBuilder(MESSAGES.accountCreated(DISPLAY))
            .create())
        .isDisplayed();
    verify(mockNavigator).navigateTo(new HomeApplicationPlace());
  }
}
