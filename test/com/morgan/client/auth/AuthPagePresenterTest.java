package com.morgan.client.auth;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.util.Providers;
import com.morgan.shared.auth.AuthenticationConstant;
import com.morgan.testing.FakeClientPageConstants;

/**
 * Tests for the {@link AuthPagePresenter} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthPagePresenterTest {

  @Mock private LoginPagePresenter mockLoginPagePresenter;
  @Mock private LogoutPagePresenter mockLogoutPagePresenter;

  private AuthPagePresenter createPresenter(boolean isLoggedIn) {
    FakeClientPageConstants constants = new FakeClientPageConstants()
        .setValue(AuthenticationConstant.IS_LOGGED_IN, Boolean.toString(isLoggedIn));
    return new AuthPagePresenter(
        constants,
        Providers.of(mockLoginPagePresenter),
        Providers.of(mockLogoutPagePresenter));
  }

  @Test public void presentPageFor_loggedIn_showsLogoutPage() {
    assertThat(createPresenter(true).presentPageFor(null)).isEqualTo(mockLogoutPagePresenter);
  }

  @Test public void presentPageFor_loggedOut_showsLogInPage() {
    assertThat(createPresenter(false).presentPageFor(null)).isEqualTo(mockLoginPagePresenter);
  }
}
