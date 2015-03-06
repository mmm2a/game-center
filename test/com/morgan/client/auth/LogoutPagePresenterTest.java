package com.morgan.client.auth;

import static com.google.common.truth.Truth.assertAbout;
import static org.mockito.Mockito.verify;
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
import com.morgan.client.auth.LogoutPagePresenter.LogoutPageView;
import com.morgan.client.constants.ClientPageConstants;
import com.morgan.client.nav.Navigator;
import com.morgan.shared.auth.AuthenticationConstant;
import com.morgan.shared.auth.AuthenticationServiceAsync;
import com.morgan.testing.FakeAlertController;
import com.morgan.testing.FakeAlertController.FakeAlert;
import com.morgan.testing.FakeClientPageConstants;
import com.morgan.testing.FakeMessagesFactory;

/**
 * Tests for the {@link LogoutPagePresenter} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class LogoutPagePresenterTest {

  private static final AuthenticationMessages MESSAGES =
      FakeMessagesFactory.create(AuthenticationMessages.class);

  private static final String EMAIL = "email address";

  @Mock private AuthenticationServiceAsync mockService;
  @Mock private LogoutPageView mockView;
  @Mock private Navigator mockNavigator;

  @Mock private HasClickHandlers mockLogoutControl;

  @Captor private ArgumentCaptor<ClickHandler> clickHandlerCaptor;
  @Captor private ArgumentCaptor<AsyncCallback<Void>> logoutCallbackCaptor;

  private FakeAlertController alertController;
  private FakeAlert loggingOutAlert;

  @Before public void createTestInstances() {
    alertController = new FakeAlertController();
    loggingOutAlert = alertController.newStatusAlertBuilder(MESSAGES.loggingOutStatus())
        .isFading(false)
        .create();

    ClientPageConstants constants = new FakeClientPageConstants()
        .setValue(AuthenticationConstant.CURRENT_USER_EMAIL, EMAIL);

    when(mockView.getLogoutControl()).thenReturn(mockLogoutControl);

    new LogoutPagePresenter(
        MESSAGES, alertController, mockService, constants, mockView, mockNavigator);
  }

  private ClickHandler verifyClickHandlerRegistered() {
    verify(mockLogoutControl).addClickHandler(clickHandlerCaptor.capture());
    return clickHandlerCaptor.getValue();
  }

  @Test public void construction_setsEmailAddress() {
    verify(mockView).setEmailAddress(EMAIL);
  }

  @Test public void construction_registersClickHandler() {
    verifyClickHandlerRegistered();
  }

  private AsyncCallback<Void> verifyCallbackRegistered() {
    verifyClickHandlerRegistered().onClick(null);
    verify(mockService).logout(logoutCallbackCaptor.capture());
    return logoutCallbackCaptor.getValue();
  }

  @Test public void onClick_callsService() {
    verifyCallbackRegistered();
    assertAbout(FakeAlertController.ALERT)
        .that(loggingOutAlert)
        .isDisplayed();
  }

  @Test public void onLogoutSucceeds() {
    verifyCallbackRegistered().onSuccess(null);
    assertAbout(FakeAlertController.ALERT)
        .that(loggingOutAlert)
        .isNotDisplayed();
    verify(mockNavigator).reload();
  }

  @Test public void onLogoutError() {
    verifyCallbackRegistered().onFailure(new NullPointerException());
    assertAbout(FakeAlertController.ALERT)
        .that(loggingOutAlert)
        .isNotDisplayed();
    assertAbout(FakeAlertController.ALERT)
        .that(alertController.newErrorAlertBuilder(MESSAGES.logOutFailed())
            .create())
        .isDisplayed();
  }
}
