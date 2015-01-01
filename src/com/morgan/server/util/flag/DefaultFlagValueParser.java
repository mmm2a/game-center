package com.morgan.server.util.flag;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;

/**
 * Default implementation of the {@link FlagValueParser} interface.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class DefaultFlagValueParser implements FlagValueParser {

  private static final Splitter COLLECTION_LIST_SPLITTER = Splitter.on(',')
      .omitEmptyStrings().trimResults();

  @Inject DefaultFlagValueParser() {
  }

  @SuppressWarnings("unchecked")
  private <T extends Enum<T>> T getEnumValue(Class<?> enumClass, String representation) {
    return Enum.valueOf((Class<T>) enumClass, representation);
  }

  private ImmutableCollection<?> parseImmutableCollection(
      Class<?> collectionType, Class<?> elementType, String representation) {
    ImmutableCollection.Builder<Object> builder;

    if (collectionType.equals(ImmutableList.class)
        || collectionType.equals(ImmutableCollection.class)) {
      builder = ImmutableList.builder();
    } else if (collectionType.equals(ImmutableSet.class)) {
      builder = ImmutableSet.builder();
    } else {
      throw new IllegalStateException(String.format(
          "Don't know how to create an immutable collection of type %s", collectionType));
    }

    for (String elementRep : COLLECTION_LIST_SPLITTER.split(representation)) {
      builder.add(parseStringRepresentation(elementType, elementRep));
    }

    return builder.build();
  }

  @Override public Object parseStringRepresentation(Type valueType, String representation) {
    if (valueType instanceof Class) {
      Class<?> valueClass = (Class<?>) valueType;
      if (valueClass.isAssignableFrom(String.class)) {
        return representation;
      }

      if (valueClass.isEnum()) {
        return getEnumValue(valueClass, representation);
      }

      if (valueClass.equals(Boolean.class) || valueClass.equals(boolean.class)) {
        return Boolean.parseBoolean(representation);
      }

      if (valueClass.equals(Integer.class) || valueClass.equals(int.class)) {
        return Integer.parseInt(representation);
      }
    } else if (valueType instanceof ParameterizedType) {
      ParameterizedType pType = (ParameterizedType) valueType;
      Type rawType = pType.getRawType();
      Preconditions.checkState(
          rawType instanceof Class, "Can't figure out what to do with flag type %s", valueType);
      Class<?> rawClass = (Class<?>) rawType;
      if (ImmutableCollection.class.isAssignableFrom(rawClass)) {
        Type[] typeParameters = pType.getActualTypeArguments();
        Preconditions.checkState(
            typeParameters.length == 1,
            "Expected only 1 type parameter for ImmutableCollection for flag type %s", valueType);
        Preconditions.checkState(
            typeParameters[0] instanceof Class,
            "Can't create a flag accessor for a flag of type ImmutableCollection with a wildcard: "
            + "Flag value was %s and type was %s", representation, valueType);
        return parseImmutableCollection(rawClass, (Class<?>) typeParameters[0], representation);
      }
    }

    throw new IllegalStateException(String.format(
        "Don't know how to parse a flag value of %s into a %s", representation, valueType));
  }
}
