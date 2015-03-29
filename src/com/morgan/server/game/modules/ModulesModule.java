package com.morgan.server.game.modules;

import com.google.common.util.concurrent.Service;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * GUICE module for the modules package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class ModulesModule extends AbstractModule {

  @Override protected void configure() {
    Multibinder.newSetBinder(binder(), Service.class)
        .addBinding().to(GamePortals.class);
  }
}
