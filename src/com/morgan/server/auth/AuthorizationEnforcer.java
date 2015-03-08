package com.morgan.server.auth;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.morgan.server.common.CommonBindingAnnotations.RequestUser;
import com.morgan.server.util.log.AdvancedLogger;
import com.morgan.shared.common.PermissionDeniedException;
import com.morgan.shared.common.Role;

/**
 * AOP {@link MethodInterceptor} to check that method calls annotated with the {@link AuthorizedFor}
 * are enforced.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class AuthorizationEnforcer implements MethodInterceptor {

  private static final AdvancedLogger log = new AdvancedLogger(AuthorizationEnforcer.class);

  @Inject @RequestUser private Provider<Optional<UserInformation>> requestUserProvider;

  @VisibleForTesting AuthorizationEnforcer(
      Provider<Optional<UserInformation>> requestUserProvider) {
    this.requestUserProvider = requestUserProvider;
  }

  AuthorizationEnforcer() {
  }

  @Override public Object invoke(MethodInvocation inv) throws Throwable {
    AuthorizedFor authorizedFor = inv.getMethod().getAnnotation(AuthorizedFor.class);
    Preconditions.checkState(authorizedFor != null);

    Optional<UserInformation> currentUser = requestUserProvider.get();
    Role effectiveRole = currentUser.or(
        new UserInformation(-1L, "not used", "not used", Role.UNKNOWN)).getUserRole();

    if (effectiveRole.compareTo(authorizedFor.value()) <= 0) {
      return inv.proceed();
    }

    log.warning("Unauthorized user %s attempted to call method %s on %s and was denied",
        (currentUser.isPresent() ? String.format(
            "%s <%s>", currentUser.get().getDisplayName(), currentUser.get().getEmailAddress())
            : "<unknown user>"),
        inv.getMethod().getName(), inv.getMethod().getDeclaringClass());

    throw new PermissionDeniedException();
  }
}
