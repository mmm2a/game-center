package com.morgan.server.util.guavato8;

import com.google.common.base.Preconditions;

/**
 * Helper class for helping convert Guava {@link com.google.common.base.Predicate} instances into
 * Java 8 {@link java.util.function.Predicate} instances.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class PredicateTranslations {

  private PredicateTranslations() {
    // Do not instantiate.
  }

  /**
   * Convert a Java 8 {@link java.util.function.Predicate} into a Guava
   * {@link com.google.common.base.Predicate}.
   */
  public static <I> com.google.common.base.Predicate<I> toGuava(
      java.util.function.Predicate<I> input) {
    Preconditions.checkNotNull(input);
    return i -> input.test(i);
  }

  /**
   * Convert a Guava {@link com.google.common.base.Predicate} into a Java 8
   * {@link java.util.function.Predicate}.
   */
  public static <I> java.util.function.Predicate<I> toJava8(
      com.google.common.base.Predicate<I> input) {
    Preconditions.checkNotNull(input);
    return i -> input.apply(i);
  }
}
