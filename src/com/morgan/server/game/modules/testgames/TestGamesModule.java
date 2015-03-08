package com.morgan.server.game.modules.testgames;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.morgan.server.game.modules.GameModule;

public class TestGamesModule extends AbstractModule {

  @Override protected void configure() {
    Multibinder<GameModule> moduleBinder = Multibinder.newSetBinder(binder(), GameModule.class);
    moduleBinder.addBinding().to(Game1Module.class);
    moduleBinder.addBinding().to(Game2Module.class);
    moduleBinder.addBinding().to(Game3Module.class);
  }
}
