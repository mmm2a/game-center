package com.morgan.server.auth;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.morgan.server.constants.PageConstants;
import com.morgan.server.constants.PageConstantsSource;
import com.morgan.server.util.soy.SoyTemplateFactory;
import com.morgan.shared.auth.AuthConstant;

/**
 * GUICE module for the auth package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AuthModule extends AbstractModule {

  @Override protected void configure() {
    install(new AuthHostPageServletModule());

    Multibinder<PageConstantsSource<AuthConstant>> binder = Multibinder.newSetBinder(
        binder(), new TypeLiteral<PageConstantsSource<AuthConstant>>(){});
    binder.addBinding().to(HelloMessagePageConstantsSource.class);
    binder.addBinding().to(VersionPageConstantsSource.class);
  }

  @Provides @Singleton
  protected AuthSoyTemplate provideAuthSoyTemplate(SoyTemplateFactory factory) {
    return factory.createSoyTemplate(AuthSoyTemplate.class);
  }

  static class HelloMessagePageConstantsSource implements PageConstantsSource<AuthConstant> {
    @Inject HelloMessagePageConstantsSource() {
    }

    @Override public void provideConstantsInto(PageConstants constantsSink) {
      constantsSink.add(AuthConstant.HELLO_MSG, "Hello, World!");
    }
  }

  static class VersionPageConstantsSource implements PageConstantsSource<AuthConstant> {

    @Inject VersionPageConstantsSource() {
    }

    @Override public void provideConstantsInto(PageConstants constantsSink) {
      constantsSink.add(AuthConstant.VERSION, 7);
    }
  }
}
