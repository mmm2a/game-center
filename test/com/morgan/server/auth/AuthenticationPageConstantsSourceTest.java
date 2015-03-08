package com.morgan.server.auth;

import static org.mockito.Mockito.verify;

import javax.annotation.Nullable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Optional;
import com.google.inject.util.Providers;
import com.morgan.server.constants.PageConstants;
import com.morgan.shared.auth.AuthenticationConstant;
import com.morgan.shared.common.Role;

/**
 * Tests for the {@link AuthenticationPageConstantsSource} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationPageConstantsSourceTest {

  @Mock private PageConstants mockSink;

  private static final long USER_ID = 7l;
  private static final String EMAIL = "email address";
  private static final String DISPLAY = "display name";

  private AuthenticationPageConstantsSource createSource(@Nullable UserInformation userInfo) {
    return new AuthenticationPageConstantsSource(Providers.of(Optional.fromNullable(userInfo)));
  }

  private UserInformation createUserInfo(Role role) {
    return new UserInformation(USER_ID, DISPLAY, EMAIL, role);
  }

  @Test public void provideConstantsInto_isLoggedIn_asAdmin() {
    createSource(createUserInfo(Role.ADMIN)).provideConstantsInto(mockSink);
    verify(mockSink).add(AuthenticationConstant.IS_LOGGED_IN, true);
    verify(mockSink).add(AuthenticationConstant.IS_ADMIN, true);
    verify(mockSink).add(AuthenticationConstant.CURRENT_USER_DISPLAY_NAME, DISPLAY);
    verify(mockSink).add(AuthenticationConstant.CURRENT_USER_EMAIL, EMAIL);
  }

  @Test public void provideConstantsInto_isLoggedIn_asMember() {
    createSource(createUserInfo(Role.MEMBER)).provideConstantsInto(mockSink);
    verify(mockSink).add(AuthenticationConstant.IS_LOGGED_IN, true);
    verify(mockSink).add(AuthenticationConstant.IS_ADMIN, false);
    verify(mockSink).add(AuthenticationConstant.CURRENT_USER_DISPLAY_NAME, DISPLAY);
    verify(mockSink).add(AuthenticationConstant.CURRENT_USER_EMAIL, EMAIL);
  }

  @Test public void provideConstantsInto_isLoggedOut() {
    createSource(null).provideConstantsInto(mockSink);
    verify(mockSink).add(AuthenticationConstant.IS_LOGGED_IN, false);
    verify(mockSink).add(AuthenticationConstant.IS_ADMIN, false);
  }
}
