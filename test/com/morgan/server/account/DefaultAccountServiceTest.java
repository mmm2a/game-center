package com.morgan.server.account;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Function;
import com.morgan.server.auth.UserInformation;
import com.morgan.server.backend.UserBackend;
import com.morgan.shared.auth.ClientUserInformation;
import com.morgan.shared.common.Role;

/**
 * Tests for the {@link DefaultAccountService} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultAccountServiceTest {

  private static final String EMAIL = "email@tempuri.org";
  private static final String DISPLAY = "Display name";
  private static final Role ROLE = Role.MEMBER;
  private static final String PASSWORD = "password";
  private static final long USER_ID = 7L;

  private static final UserInformation USER_INFO =
      new UserInformation(USER_ID, DISPLAY, EMAIL, ROLE);
  private static final ClientUserInformation CLIENT_USER_INFO =
      ClientUserInformation.withPrivlidgedInformation(DISPLAY, ROLE);

  @Mock private UserBackend mockBackend;
  @Mock private PasswordCreator mockPasswordCreator;
  @Mock private Function<UserInformation, ClientUserInformation> mockUserInformationConverter;

  private DefaultAccountService service;

  @Before public void createTestInstances() {
    service = new DefaultAccountService(
        mockBackend,
        mockPasswordCreator,
        mockUserInformationConverter);
  }

  @Before public void setUpCommonMockInteractions() throws Exception {
    when(mockPasswordCreator.get()).thenReturn(PASSWORD);
    when(mockBackend.createAccount(EMAIL, DISPLAY, PASSWORD, ROLE)).thenReturn(USER_INFO);
    when(mockUserInformationConverter.apply(USER_INFO)).thenReturn(CLIENT_USER_INFO);
  }

  @Test public void createAccount_returnsCorrectInformation() throws Exception {
    assertThat(service.createAccount(EMAIL, DISPLAY, ROLE))
        .isEqualTo(CLIENT_USER_INFO);
  }
}
