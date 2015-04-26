package com.morgan.server.util.guavato8;

import com.google.common.base.Preconditions;

/**
 * Helper class to help convert between GUAVA {@link com.google.common.base.Function} instances and
 * Java 8 {@link java.util.function.Function} instances.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class FunctionTranslations {

  private FunctionTranslations() {
    // Do not instantiate.
  }

  /**
   * Converts a Java 8 {@link java.util.function.Function} into a
   * Guava {@link com.google.common.base.Function}.
   */
  public <I, O> com.google.common.base.Function<I, O> toGuava(
      java.util.function.Function<I, O> input) {
    Preconditions.checkNotNull(input);
    return i -> input.apply(i);
  }

  /**
   * Converts a Guava {@link com.google.common.base.Function} into a
   * Java 8 {@link java.util.function.Function}.
   */
  public <I, O> java.util.function.Function<I, O> toJava8(
      com.google.common.base.Function<I, O> input) {
    Preconditions.checkNotNull(input);
    return i -> input.apply(i);
  }
}
