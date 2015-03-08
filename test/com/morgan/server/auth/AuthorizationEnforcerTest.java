package com.morgan.server.auth;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import javax.annotation.Nullable;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Optional;
import com.google.inject.util.Providers;
import com.morgan.shared.common.PermissionDeniedException;
import com.morgan.shared.common.Role;

/**
 * Tests for the {@link AuthorizationEnforcer} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthorizationEnforcerTest {

  private static final Method METHOD;
  private static final Object PROCEED = new Object();

  static {
    try {
      METHOD = TestClass.class.getMethod("method");
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @Mock private MethodInvocation mockInv;

  @Before public void setUpCommonMockInteractions() throws Throwable {
    when(mockInv.getMethod()).thenReturn(METHOD);
    when(mockInv.proceed()).thenReturn(PROCEED);
  }

  private AuthorizationEnforcer createEnforcer(@Nullable Role role) {
    if (role == null) {
      return new AuthorizationEnforcer(Providers.of(Optional.<UserInformation>absent()));
    } else {
      return new AuthorizationEnforcer(Providers.of(Optional.of(
          new UserInformation(0L, "display name", "email address", role))));
    }
  }

  @Test public void invoke_isExactlyAuthorized() throws Throwable {
    assertThat(createEnforcer(Role.MEMBER).invoke(mockInv)).isSameAs(PROCEED);
    verify(mockInv).proceed();
  }

  @Test public void invoke_isMoreThanAuthorized() throws Throwable {
    assertThat(createEnforcer(Role.ADMIN).invoke(mockInv)).isSameAs(PROCEED);
    verify(mockInv).proceed();
  }

  @Test(expected = PermissionDeniedException.class)
  public void invoke_isUnderAuthorized() throws Throwable {
    createEnforcer(Role.UNKNOWN).invoke(mockInv);
  }

  @Test(expected = PermissionDeniedException.class)
  public void invoke_isNotLoggedIn() throws Throwable {
    createEnforcer(null).invoke(mockInv);
  }

  private static class TestClass {

    @AuthorizedFor(Role.MEMBER)
    public void method() {
    }
  }
}
