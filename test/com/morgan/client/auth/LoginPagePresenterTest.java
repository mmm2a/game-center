package com.morgan.client.auth;

import static com.google.common.truth.Truth.assertAbout;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.morgan.client.auth.LoginPagePresenter.LoginPageView;
import com.morgan.shared.auth.AuthenticationServiceAsync;
import com.morgan.testing.FakeAlertController;
import com.morgan.testing.FakeAlertController.FakeAlert;
import com.morgan.testing.FakeMessagesFactory;

/**
 * Tests for the {@link LoginPagePresenter} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginPagePresenterTest {

  private static final AuthenticationMessages MESSAGES =
      FakeMessagesFactory.create(AuthenticationMessages.class);
  
  private static final String EMAIL_ADDRESS = "email address";
  private static final String PASSWORD = "password";
  
  @Mock private LoginPageView mockView;
  @Mock private AuthenticationServiceAsync mockService;
  @Mock private HasClickHandlers mockLoginControl;
  
  @Captor private ArgumentCaptor<ClickHandler> clickHandlerCaptor;
  @Captor private ArgumentCaptor<AsyncCallback<Boolean>> callbackCaptor;
  
  private FakeAlertController alertController;
  
  private FakeAlert loggingInStatus;
  
  @Before public void createTestInstances() {
    alertController = new FakeAlertController();
    loggingInStatus = alertController.newStatusAlertBuilder(MESSAGES.loggingInStatus())
        .isFading(false)
        .create();
    
    when(mockView.getLoginButtonControl()).thenReturn(mockLoginControl);
    
    new LoginPagePresenter(
        MESSAGES,
        alertController,
        mockService,
        mockView);
  }
  
  @Before public void setUpCommonMockInteractions() {
    when(mockView.getEmailAddress()).thenReturn(EMAIL_ADDRESS);
    when(mockView.getPassword()).thenReturn(PASSWORD);
  }
  
  private ClickHandler verifyClickHandler() {
    verify(mockLoginControl).addClickHandler(clickHandlerCaptor.capture());
    return clickHandlerCaptor.getValue();
  }
  
  private AsyncCallback<Boolean> verifyLoginCalled() {
    verifyClickHandler().onClick(null);
    verify(mockService).authenticate(
        eq(EMAIL_ADDRESS),
        eq(PASSWORD),
        callbackCaptor.capture());
    return callbackCaptor.getValue();
  }
  
  @Test public void construction_clickHandlerRegistered() {
    verifyClickHandler();
  }
  
  @Test public void click_loginCallMade() {
    verifyLoginCalled();
    assertAbout(FakeAlertController.ALERT)
        .that(loggingInStatus)
        .isDisplayed();
  }
  
  @Test @Ignore public void loginSucceeds() {
    verifyLoginCalled().onSuccess(true);
    assertAbout(FakeAlertController.ALERT)
        .that(loggingInStatus)
        .isNotDisplayed();
  }
  
  @Test public void loginFailed() {
    verifyLoginCalled().onSuccess(false);
    assertAbout(FakeAlertController.ALERT)
        .that(loggingInStatus)
        .isNotDisplayed();
    assertAbout(FakeAlertController.ALERT)
        .that(alertController.newErrorAlertBuilder(MESSAGES.logInFailed())
            .create())
        .isDisplayed();
  }
  
  @Test public void loginError() {
    verifyLoginCalled().onFailure(new NullPointerException());
    assertAbout(FakeAlertController.ALERT)
        .that(loggingInStatus)
        .isNotDisplayed();
    assertAbout(FakeAlertController.ALERT)
        .that(alertController.newErrorAlertBuilder(MESSAGES.logInError())
            .create())
        .isDisplayed();
  }
}
