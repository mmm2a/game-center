package com.morgan.server.util.log;

import java.lang.reflect.Field;

import com.google.inject.MembersInjector;

/**
 * A {@link MembersInjector} for injecting {@link AdvancedLogger} instances.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class AdvancedLoggerMemberInjector<T> implements MembersInjector<T>{
  private final Field field;
  private final AdvancedLogger logger;

  AdvancedLoggerMemberInjector(Field field) {
    this.field = field;
    this.logger = new AdvancedLogger(field.getDeclaringClass());
    field.setAccessible(true);
  }

  @Override public void injectMembers(T t) {
    try {
      field.set(t, logger);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
