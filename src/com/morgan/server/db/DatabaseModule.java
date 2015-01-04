package com.morgan.server.db;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.morgan.server.util.flag.FlagAccessorFactory;

/**
 * A GUICE module for adding a J2EE style persistence unit to the guice injection tree.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class DatabaseModule extends AbstractModule {

  @Override protected void configure() {
    DbFlagAccessor accessor = FlagAccessorFactory.getNonInjectedInstance()
        .getFlagAccessor(DbFlagAccessor.class);

    install(new JpaPersistModule(accessor.persistenceUnit()));

    bind(DatabaseInitializationService.class).asEagerSingleton();
  }

  @Provides @Singleton protected DbFlagAccessor provideDbFlagAccessor(FlagAccessorFactory factory) {
    return factory.getFlagAccessor(DbFlagAccessor.class);
  }
}
