package com.morgan.server.game;

import java.util.concurrent.Executors;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
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

    bind(EventBus.class).in(Singleton.class);
  }

  @Provides @Singleton @Background
  protected ListeningExecutorService provideBackgroundExecutorService() {
    return MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
  }

  @Provides @Singleton
  protected ListeningScheduledExecutorService provideScheduledExecutorService() {
    return MoreExecutors.listeningDecorator(Executors.newScheduledThreadPool(4));
  }
}
