package com.morgan.server.game;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;

import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.morgan.server.account.AccountModule;
import com.morgan.server.alarm.AlarmModule;
import com.morgan.server.auth.AuthModule;
import com.morgan.server.backend.BackendModule;
import com.morgan.server.common.CommonBindingAnnotations.Background;
import com.morgan.server.email.EmailModule;
import com.morgan.server.game.modules.ModulesLoader;
import com.morgan.server.nav.NavigationModule;
import com.morgan.server.polymer.PolymerModule;
import com.morgan.server.security.SecurityModule;
import com.morgan.server.util.flag.FlagAccessorFactory;
import com.morgan.server.util.log.LogModule;
import com.morgan.server.util.mime.MimeModule;
import com.morgan.server.util.stat.StatModule;
import com.morgan.shared.game.modules.GameIdentifier;

/**
 * Main GUICE module for the game server.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class GameModule extends AbstractModule {

  @Override protected void configure() {
    install(new AccountModule());
    install(new AuthModule());
    install(new GameAppModule());
    install(new GameServletsModule());
    install(new SecurityModule());
    install(new BackendModule());
    install(new EmailModule());
    install(new AlarmModule());
    install(new NavigationModule());
    install(new LogModule());
    install(new MimeModule());
    install(new PolymerModule());
    install(new StatModule());

    bind(EventBus.class).in(Singleton.class);

    ImmutableSet.Builder<GameIdentifier> gameIdentifiersBuilder = ImmutableSet.builder();
    ModulesLoader loader = new ModulesLoader();
    try {
      for (Map.Entry<GameIdentifier, Module> entry : loader.loadModules().entrySet()) {
        install(entry.getValue());
        gameIdentifiersBuilder.add(entry.getKey());
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    bind(new TypeLiteral<ImmutableSet<GameIdentifier>>() {})
        .toInstance(gameIdentifiersBuilder.build());
  }

  @Provides @Singleton @Background
  protected ListeningExecutorService provideBackgroundExecutorService() {
    return MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
  }

  @Provides @Singleton
  protected ListeningScheduledExecutorService provideScheduledExecutorService() {
    return MoreExecutors.listeningDecorator(Executors.newScheduledThreadPool(4));
  }

  @Provides @Singleton protected Random provideRandomGenerator() {
    return new SecureRandom();
  }

  @Provides @Singleton protected GameServerFlagAccessor provideGameServerFlagAccessor(
      FlagAccessorFactory accessorFactory) {
    return accessorFactory.getFlagAccessor(GameServerFlagAccessor.class);
  }
}
