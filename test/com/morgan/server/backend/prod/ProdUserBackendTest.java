package com.morgan.server.backend.prod;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
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

  @Test public void findUsersById() throws BackendException {
    UserInformation userInformation1 = new UserInformation(
        7L, "display 1", "email address 1", Role.MEMBER);
    UserInformation userInformation2 = new UserInformation(
        42L, "display 2", "email address 2", Role.MEMBER);
    UserInformation userInformation3 = new UserInformation(
        69L, "display 3", "email address 3", Role.MEMBER);

    ImmutableSet<Long> ids = ImmutableSet.of(7L, 42L, 69L);
    ImmutableMap<Long, UserInformation> result = ImmutableMap.of(
        7L, userInformation1,
        42L, userInformation2,
        69L, userInformation3);

    when(mockHelper.findUsersById(ids)).thenReturn(result);

    result = backend.findUsersById(ids);
    assertThat(result).hasSize(3);
    assertThat(result).containsEntry(7L, userInformation1);
    assertThat(result).containsEntry(42L, userInformation2);
    assertThat(result).containsEntry(69L, userInformation3);
  }

  @Test public void createAccount() throws BackendException {
    UserInformation userInfo =
        new UserInformation(7L, "display name", "email address", Role.MEMBER);
    when(mockHelper.createAccount("in email", "in display", "in password", Role.ADMIN))
        .thenReturn(userInfo);
    assertThat(backend.createAccount("in email", "in display", "in password", Role.ADMIN))
        .isSameAs(userInfo);
  }
}
