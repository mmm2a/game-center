package com.morgan.client.account;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.common.truth.Truth.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.morgan.shared.auth.AuthenticationConstant;
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

  @Mock private AccountCreationPagePresenter.View mockView;

  private FakeAlertController alerts;
  private FakeClientPageConstants constants;

  @Before public void createTestInstances() {
    alerts = new FakeAlertController();
    constants = new FakeClientPageConstants();
  }

  private AccountCreationPagePresenter createPresenter(boolean isAdmin) {
    constants.setValue(AuthenticationConstant.IS_ADMIN, Boolean.toString(isAdmin));
    return new AccountCreationPagePresenter(MESSAGES, alerts, mockView, constants);
  }

  @Test public void presentPageFor_notAdmin_showsError() {
    assertThat(createPresenter(false).presentPageFor(null)).isAbsent();
    assertAbout(FakeAlertController.ALERT)
        .that(alerts.newErrorAlertBuilder(MESSAGES.accountCreationNotPermitted()).create())
        .isDisplayed();
  }

  @Test public void presentPageFor_isAdmin_showsView() {
    assertThat(createPresenter(true).presentPageFor(null)).hasValue(mockView);
    assertAbout(FakeAlertController.ALERT)
        .that(alerts.newErrorAlertBuilder(MESSAGES.accountCreationNotPermitted()).create())
        .isNotDisplayed();
  }
}
