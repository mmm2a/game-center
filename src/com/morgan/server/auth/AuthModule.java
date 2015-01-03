package com.morgan.server.auth;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.morgan.server.util.soy.SoyTemplateFactory;

/**
 * GUICE module for the auth package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AuthModule extends AbstractModule {

  @Override protected void configure() {
    install(new AuthHostPageServletModule());
  }

  @Provides @Singleton
  protected AuthSoyTemplate provideAuthSoyTemplate(SoyTemplateFactory factory) {
    return factory.createSoyTemplate(AuthSoyTemplate.class);
  }
}
