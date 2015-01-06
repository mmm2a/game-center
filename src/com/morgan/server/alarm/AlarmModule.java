package com.morgan.server.alarm;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.morgan.server.util.common.Service;

/**
 * GUICE module for the alarm package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AlarmModule extends AbstractModule {

  @Override protected void configure() {
    Multibinder.newSetBinder(binder(), Service.class)
        .addBinding().to(DefaultAlarmManager.class);
  }
}
