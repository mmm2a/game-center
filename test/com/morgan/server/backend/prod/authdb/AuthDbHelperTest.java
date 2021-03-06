package com.morgan.server.backend.prod.authdb;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.util.Providers;
import com.morgan.server.auth.UserInformation;
import com.morgan.shared.common.Role;

/**
 * Tests for the {@link AuthDbHelper} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthDbHelperTest {

  private static final long USER_ID = 7L;
  private static final String EMAIL = "email address";
  private static final String DISPLAY_NAME = "display name";
  private static final Role ROLE = Role.MEMBER;

  @Mock private EntityManager mockEntityManager;
  @Mock private TypedQuery<UserInformationEntity> mockQuery;

  @Captor private ArgumentCaptor<UserInformationEntity> userInfoEntityCaptor;

  private AuthDbHelper helper;

  @Before public void createTestInstances() {
    helper = new AuthDbHelper(Providers.of(mockEntityManager));
  }

  @Before public void setUpCommonMockInteractions() {
    when(mockQuery.setParameter(anyString(), anyObject())).thenReturn(mockQuery);
  }

  @Test public void findUserById_noSuchUser_returnsAbsent() {
    assertThat(helper.findUserById(USER_ID)).isAbsent();
  }

  @Test public void findUserById() {
    when(mockEntityManager.find(UserInformationEntity.class, USER_ID))
        .thenReturn(new UserInformationEntity(USER_ID, EMAIL, DISPLAY_NAME, ROLE));

    assertThat(helper.findUserById(7))
        .hasValue(new UserInformation(USER_ID, DISPLAY_NAME, EMAIL, ROLE));
  }

  @Test public void findUsersById() {
    ImmutableSet<Long> ids = ImmutableSet.of(7L, 42L, 69L);
    when(mockEntityManager.find(UserInformationEntity.class, 7L))
        .thenReturn(new UserInformationEntity(7L, "email1", "display 1", ROLE));
    when(mockEntityManager.find(UserInformationEntity.class, 69L))
        .thenReturn(new UserInformationEntity(69L, "email3", "display 3", ROLE));
    ImmutableMap<Long, UserInformation> result = helper.findUsersById(ids);
    assertThat(result).hasSize(2);
    assertThat(result).containsEntry(7L, new UserInformation(7L, "display 1", "email1", ROLE));
    assertThat(result).containsEntry(69L, new UserInformation(69L, "display 3", "email3", ROLE));
  }

  @Test public void authenticate_noSuchUser_returnsAbsent() {
    when(mockEntityManager.createNamedQuery("findUserByEmail", UserInformationEntity.class))
        .thenReturn(mockQuery);
    when(mockQuery.getResultList()).thenReturn(ImmutableList.<UserInformationEntity>of());
    assertThat(helper.authenticate(EMAIL, "password")).isAbsent();
  }

  @Test public void authenticate_wrongPassword_returnsAbsent() {
    when(mockEntityManager.createNamedQuery("findUserByEmail", UserInformationEntity.class))
        .thenReturn(mockQuery);
    when(mockQuery.getResultList()).thenReturn(ImmutableList.of(
        new UserInformationEntity(USER_ID, EMAIL, DISPLAY_NAME, ROLE)
            .setAuthenticationEntity(new AuthenticationEntity(USER_ID, "password"))));
    assertThat(helper.authenticate(EMAIL, "wrong password")).isAbsent();
  }

  @Test public void authenticate() {
    when(mockEntityManager.createNamedQuery("findUserByEmail", UserInformationEntity.class))
        .thenReturn(mockQuery);
    when(mockQuery.getResultList()).thenReturn(ImmutableList.of(
        new UserInformationEntity(USER_ID, EMAIL, DISPLAY_NAME, ROLE)
            .setAuthenticationEntity(new AuthenticationEntity(USER_ID, "password"))));
    assertThat(helper.authenticate(EMAIL, "password"))
        .hasValue(new UserInformation(USER_ID, DISPLAY_NAME, EMAIL, ROLE));
  }

  @Test public void createAccount() throws Exception {
    String email = "email address";
    String display = "display name";
    String password = "password";
    Role role = Role.MEMBER;

    assertThat(helper.createAccount(email, display, password, role))
        .isEqualTo(new UserInformation(0L, display, email, role));
    verify(mockEntityManager).persist(userInfoEntityCaptor.capture());
    UserInformationEntity entity = userInfoEntityCaptor.getValue();
    assertThat(entity.getEmailAddress()).isEqualTo(email);
    assertThat(entity.getDisplayName()).isEqualTo(display);
    assertThat(entity.getRole()).isEqualTo(role);
    assertThat(entity.getAuthenticationEntity().getPassword()).isEqualTo(password);
  }
}
