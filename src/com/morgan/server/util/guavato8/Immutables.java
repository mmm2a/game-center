package com.morgan.server.util.guavato8;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * A class to aid with translating Java 8 features to Immutable*** features.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class Immutables {

  private Immutables() {
    // Do not instantiate.
  }

  /**
   * Wraps a {@link ImmutableCollection.Builder} in a {@link Consumer} facade.
   */
  public static <T> Consumer<T> toConsumer(ImmutableCollection.Builder<? super T> builder) {
    Preconditions.checkNotNull(builder);
    return i -> builder.add(i);
  }

  /**
   * Create an {@link ImmutableCollection} from a {@link Stream}.
   */
  public static <T> ImmutableCollection<T> collectionFrom(Stream<? extends T> stream) {
    return ImmutableList.<T>copyOf(stream.iterator());
  }

  /**
   * Create an {@link ImmutableSet} from a {@link Stream}.
   */
  public static <T> ImmutableSet<T> setFrom(Stream<? extends T> stream) {
    return ImmutableSet.<T>copyOf(stream.iterator());
  }

  /**
   * Create an {@link ImmutableList} from a {@link Stream}.
   */
  public static <T> ImmutableList<T> listFrom(Stream<? extends T> stream) {
    return ImmutableList.<T>copyOf(stream.iterator());
  }

  /**
   * Creates an {@link ImmutableMap} from a {@link Stream} of values, and a {@link Function} that
   * translates values into a key.
   */
  public static <K, V> ImmutableMap<K, V> mapFrom(
      Stream<? extends V> stream,
      Function<? super V, ? extends K> valueToKeyMapper) {
    Preconditions.checkNotNull(valueToKeyMapper);

    ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
    stream.forEach(v -> builder.put(valueToKeyMapper.apply(v), v));
    return builder.build();
  }
}
