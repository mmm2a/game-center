package com.morgan.server.backend.prod;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Optional;
import com.morgan.server.auth.UserInformation;
import com.morgan.server.backend.prod.authdb.AuthDbHelper;
import com.morgan.shared.common.BackendException;
import com.morgan.shared.common.Role;

/**
 * Tests for the {@link ProdUserBackend} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class ProdUserBackendTest {

  @Mock private AuthDbHelper mockHelper;

  private ProdUserBackend backend;

  @Before public void createTestInstances() {
    backend = new ProdUserBackend(mockHelper);
  }

  @Test public void logIn_failure_passesBackAbsent() {
    when(mockHelper.authenticate("email address", "password"))
        .thenReturn(Optional.<UserInformation>absent());
    assertThat(backend.logIn("email address", "password")).isAbsent();
  }

  @Test public void logIn_success_passesBackInfoObject() {
    UserInformation userInformation = new UserInformation(
        7L, "display", "email address", Role.MEMBER);

    when(mockHelper.authenticate("email address", "password"))
        .thenReturn(Optional.of(userInformation));
    assertThat(backend.logIn("email address", "password")).hasValue(userInformation);
  }

  @Test public void findUserById() throws BackendException {
    UserInformation userInformation = new UserInformation(
        7L, "display", "email address", Role.MEMBER);
    when(mockHelper.findUserById(7L)).thenReturn(Optional.of(userInformation));
    assertThat(backend.findUserById(7L)).hasValue(userInformation);
  }
}
