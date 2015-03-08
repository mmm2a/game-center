package com.morgan.server.util.log;

import java.lang.reflect.Field;

import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * A {@link TypeListener} for injecting the advanced logger into classes.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class AdvancedLoggerTypeListener implements TypeListener {

  @Inject AdvancedLoggerTypeListener() {
  }

  @Override public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
    Class<?> clazz = type.getRawType();
    while (clazz != null) {
      for (Field field : clazz.getDeclaredFields()) {
        if (field.getType() == AdvancedLogger.class &&
          field.isAnnotationPresent(InjectLogger.class)) {
          encounter.register(new AdvancedLoggerMemberInjector<I>(field));
        }
      }
      clazz = clazz.getSuperclass();
    }
  }
}
