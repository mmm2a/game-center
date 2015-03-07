package com.morgan.server.auth;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Optional;
import com.morgan.server.backend.UserBackend;
import com.morgan.shared.common.BackendException;
import com.morgan.shared.common.Role;

/**
 * Tests for the {@link AuthAppModule} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthModuleTest {

  @Mock private UserBackend mockBackend;

  private AuthAppModule module;

  @Before public void createTestInstances() {
    module = new AuthAppModule();
  }

  @Test public void provideRequestUserInformation_userIdNotPresent_noUserInformation() {
    assertThat(module.provideRequestUserInformation(mockBackend, Optional.<Long>absent()))
        .isAbsent();
  }

  @Test public void provideRequestUserInformation_backendException_noUserInformation()
      throws BackendException {
    long id = 7L;
    when(mockBackend.findUserById(id)).thenThrow(new BackendException("backend exception"));
    assertThat(module.provideRequestUserInformation(mockBackend, Optional.of(id)))
        .isAbsent();
  }

  @Test public void provideRequestUserInformation() throws BackendException {
    long id = 7L;
    UserInformation userInfo = new UserInformation(
        id, "Display name", "Email address", Role.MEMBER);

    when(mockBackend.findUserById(id)).thenReturn(Optional.of(userInfo));
    assertThat(module.provideRequestUserInformation(mockBackend, Optional.of(id)))
        .hasValue(userInfo);
  }
}
