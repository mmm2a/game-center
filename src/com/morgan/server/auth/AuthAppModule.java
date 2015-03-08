package com.morgan.server.auth;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;
import com.morgan.server.backend.UserBackend;
import com.morgan.server.common.CommonBindingAnnotations.RequestUser;
import com.morgan.server.constants.PageConstantsSource;
import com.morgan.server.util.log.AdvancedLogger;
import com.morgan.server.util.soy.SoyTemplateFactory;
import com.morgan.shared.common.BackendException;

/**
 * GUICE module for the auth package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class AuthAppModule extends AbstractModule {

  private static final AdvancedLogger log = new AdvancedLogger(AuthAppModule.class);

  AuthAppModule() {
  }

  @Override protected void configure() {
    install(new AuthHostPageServletModule());
    install(new AuthServletsModule());

    Multibinder.newSetBinder(binder(), PageConstantsSource.class)
        .addBinding().to(AuthenticationPageConstantsSource.class);

    AuthorizationEnforcer enforcer = new AuthorizationEnforcer();
    requestInjection(enforcer);
    bindInterceptor(Matchers.any(), Matchers.annotatedWith(AuthorizedFor.class), enforcer);
  }

  @Provides @Singleton
  protected AuthSoyTemplate provideAuthSoyTemplate(SoyTemplateFactory factory) {
    return factory.createSoyTemplate(AuthSoyTemplate.class);
  }

  @Provides @RequestUser protected Optional<UserInformation> provideRequestUserInformation(
      UserBackend userBackend,
      @RequestUser Optional<Long> requestUserId) {
    if (!requestUserId.isPresent()) {
      return Optional.absent();
    }

    Optional<UserInformation> userInfo = Optional.absent();
    try {
      userInfo = userBackend.findUserById(requestUserId.get());
    } catch (BackendException e) {
      log.warning(e, "Error trying to lookup user information for user %d", requestUserId.get());
    }

    return userInfo;
  }
}
