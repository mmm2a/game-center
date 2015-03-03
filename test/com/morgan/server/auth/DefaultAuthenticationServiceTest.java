package com.morgan.server.auth;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Optional;
import com.morgan.server.backend.UserBackend;
import com.morgan.server.security.CookieHelper;
import com.morgan.shared.common.BackendException;
import com.morgan.shared.common.Role;

/**
 * Tests for the {@link DefaultAuthenticationService} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultAuthenticationServiceTest {

  private static final String EMAIL = "email address";
  private static final String PASSWORD = "password";

  @Mock private UserBackend mockBackend;
  @Mock private CookieHelper mockCookieHelper;

  private DefaultAuthenticationService service;

  @Before public void createTestInstances() {
    service = new DefaultAuthenticationService(mockBackend, mockCookieHelper);
  }

  @Test public void authenticate_failedAttempt() throws Exception {
    when(mockBackend.logIn(EMAIL, PASSWORD)).thenReturn(Optional.<UserInformation>absent());
    assertThat(service.authenticate(EMAIL, PASSWORD)).isFalse();

    verify(mockCookieHelper).invalidateCurrentCookie();
    verifyNoMoreInteractions(mockCookieHelper);;
  }

  @Test public void authenticate_successfulAttempt() throws Exception {
    when(mockBackend.logIn(EMAIL, PASSWORD))
        .thenReturn(Optional.of(new UserInformation(7L, "User Name", EMAIL, Role.ADMIN)));
    assertThat(service.authenticate(EMAIL, PASSWORD)).isTrue();

    InOrder order = inOrder(mockCookieHelper);
    order.verify(mockCookieHelper).invalidateCurrentCookie();
    order.verify(mockCookieHelper).setAuthenticationCookieFor(7L);
  }

  @Test public void logout_callsThroughToCookieHelper() throws BackendException {
    service.logout();
    verify(mockCookieHelper).logout();
  }
}
