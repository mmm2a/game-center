package com.morgan.server.game;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.morgan.server.alarm.AlarmModule;
import com.morgan.server.auth.AuthModule;
import com.morgan.server.backend.BackendModule;
import com.morgan.server.common.CommonBindingAnnotations.Background;
import com.morgan.server.email.EmailModule;
import com.morgan.server.nav.NavigationModule;
import com.morgan.server.security.SecurityModule;

/**
 * Main GUICE module for the game server.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class GameModule extends AbstractModule {

  @Override protected void configure() {
    install(new AuthModule());
    install(new GameServletsModule());
    install(new SecurityModule());
    install(new BackendModule());
    install(new EmailModule());
    install(new AlarmModule());
    install(new NavigationModule());
  }

  @Provides @Singleton @Background
  protected ExecutorService provideBackgroundExecutorService() {
    return Executors.newCachedThreadPool();
  }

  @Provides @Singleton
  protected ScheduledExecutorService provideScheduledExecutorService() {
    return Executors.newSingleThreadScheduledExecutor();
  }
}
