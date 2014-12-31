package com.morgan.server.util.flag;

import com.google.inject.Inject;

/**
 * Default implementation of the {@link FlagValueParser} interface.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class DefaultFlagValueParser implements FlagValueParser {

  @Inject DefaultFlagValueParser() {
  }

  @SuppressWarnings("unchecked")
  private <T extends Enum<T>> T getEnumValue(Class<?> enumClass, String representation) {
    return Enum.valueOf((Class<T>) enumClass, representation);
  }

  @Override public Object parseStringRepresentation(Class<?> valueType, String representation) {
    if (valueType.isAssignableFrom(String.class)) {
      return representation;
    }

    if (valueType.isEnum()) {
      return getEnumValue(valueType, representation);
    }

    if (valueType.equals(Boolean.class) || valueType.equals(boolean.class)) {
      return Boolean.parseBoolean(representation);
    }

    if (valueType.equals(Integer.class) || valueType.equals(int.class)) {
      return Integer.parseInt(representation);
    }

    throw new IllegalStateException(String.format(
        "Don't know how to parse a flag value of %s into a %s", representation, valueType));
  }
}
