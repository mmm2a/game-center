package com.morgan.server.util.guavato8;

import java.util.function.Consumer;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * Turns the various "builders" into {@link Consumer} instances.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class BuildersAsConsumers {

  /**
   * Wrapper interface for a type that is both a {@link Consumer} and a "builder" of immutable
   * collections.
   *
   * @author mark@mark-morgan.net (Mark Morgan)
   */
  public interface BuilderConsumer<T, C extends ImmutableCollection<T>> extends Consumer<T> {
    /**
     * Builder the collection that this consumer was building.
     */
    C build();
  }

  private BuildersAsConsumers() {
    // Do not instantiate.
  }

  public static <T> BuilderConsumer<T, ImmutableCollection<T>> immutableCollectionBuilder() {
    ImmutableCollection.Builder<T> builder = ImmutableList.builder();
    return new BuilderConsumer<T, ImmutableCollection<T>>() {
      @Override public void accept(T t) {
        builder.add(t);
      }

      @Override public ImmutableCollection<T> build() {
        return builder.build();
      }
    };
  }

  public static <T> BuilderConsumer<T, ImmutableList<T>> immutableListBuilder() {
    ImmutableList.Builder<T> builder = ImmutableList.builder();
    return new BuilderConsumer<T, ImmutableList<T>>() {
      @Override public void accept(T t) {
        builder.add(t);
      }

      @Override public ImmutableList<T> build() {
        return builder.build();
      }
    };
  }

  public static <T> BuilderConsumer<T, ImmutableSet<T>> immutableSetBuilder() {
    ImmutableSet.Builder<T> builder = ImmutableSet.builder();
    return new BuilderConsumer<T, ImmutableSet<T>>() {
      @Override public void accept(T t) {
        builder.add(t);
      }

      @Override public ImmutableSet<T> build() {
        return builder.build();
      }
    };
  }
}
