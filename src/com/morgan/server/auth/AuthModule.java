package com.morgan.server.auth;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.morgan.server.util.flag.FlagAccessorFactory;

/**
 * Module for the authentication package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AuthModule extends AbstractModule {

  @Override protected void configure() {
    install(new AuthAppModule());
  }

  @Provides @Singleton protected AuthFlagAccessor provideSecurityFlagAccessor(
      FlagAccessorFactory accessorFactory) {
    return accessorFactory.getFlagAccessor(AuthFlagAccessor.class);
  }
}
