package com.morgan.server.mtg.json;

import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Verify;
import com.google.common.collect.ImmutableMap;

/**
 * Gives a mapping from JSON string values to enumeration values.
 *
 * @param <T> the type of enum that we are mapping to.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class JsonEnumMapping<T extends Enum<T>> implements Function<String, T> {

  private final ImmutableMap<String, T> mapping;

  private JsonEnumMapping(Class<T> enumType, Map<String, T> mapping) {
    EnumSet<T> unSeen = EnumSet.allOf(enumType);

    this.mapping = ImmutableMap.copyOf(mapping);
    mapping.values().stream().forEach(a -> unSeen.remove(a));
    Verify.verify(unSeen.isEmpty());
  }

  @Override @Nullable public T apply(String t) {
    return mapping.get(t);
  }

  static <T extends Enum<T>> Builder<T> builderFor(Class<T> enumType) {
    return new Builder<>(enumType);
  }

  /**
   * Builder class for building {@link JsonEnumMapping} instances.
   */
  static class Builder<T extends Enum<T>> {

    private final Class<T> enumType;
    private final ImmutableMap.Builder<String, T> mappingBuilder = ImmutableMap.builder();

    private Builder(Class<T> enumType) {
      this.enumType = Preconditions.checkNotNull(enumType);
    }

    public Builder<T> addMapping(String jsonValue, T enumValue) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(jsonValue));
      Preconditions.checkNotNull(enumValue);

      mappingBuilder.put(jsonValue, enumValue);
      return this;
    }

    JsonEnumMapping<T> build() {
      return new JsonEnumMapping<>(enumType, mappingBuilder.build());
    }
  }
}
