package com.morgan.server.auth;

import com.google.common.base.Function;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.morgan.server.util.flag.FlagAccessorFactory;
import com.morgan.shared.auth.ClientUserInformation;

/**
 * Module for the authentication package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AuthModule extends AbstractModule {

  @Override protected void configure() {
    install(new AuthAppModule());

    bind(new TypeLiteral<Function<UserInformation, ClientUserInformation>>() {})
        .to(UserInformationConverter.class);
  }

  @Provides @Singleton protected AuthFlagAccessor provideSecurityFlagAccessor(
      FlagAccessorFactory accessorFactory) {
    return accessorFactory.getFlagAccessor(AuthFlagAccessor.class);
  }
}
