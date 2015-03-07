package com.morgan.server.game;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.morgan.server.util.soy.SoyTemplateFactory;

/**
 * GUICE module for the game gwt app.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class GameAppModule extends AbstractModule {

  GameAppModule() {
  }

  @Override protected void configure() {
    install(new GameHostPageServletModule());
  }

  @Provides @Singleton
  protected GameSoyTemplate provideGameSoyTemplate(SoyTemplateFactory factory) {
    return factory.createSoyTemplate(GameSoyTemplate.class);
  }
}
