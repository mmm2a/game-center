package com.morgan.server.feature;

import com.google.common.base.Optional;

/**
 * An interface for a type that checks to see if a feature descriptor enabled, disabled, or is not
 * understood for evaluation.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface ServerFeatureEvaluator {
  /**
   * Evaluates whether or not the given feature descriptor indicates enabling or disabling
   * a feature.
   *
   * @param featureDescriptor a string descriptor that is being applied to a feature to see if it
   *     should be enabled or not.  For example, for the feature configuration
   *     <code>FEATURE(foo:bar | alpha:omega)</code>, this method would be called with both
   *     foo:bar as a value, and later with alpha:omega as a value.  The OR ({@code |}) will be
   *     handled at a higher level.
   * @return an optional value where {@code true} indicates that the descriptor indicates
   *     enablement, {@code false} indicates disablement, and {@link Optional#absent()} indicates
   *     that this method can't determine the value.
   */
  Optional<Boolean> shouldEnable(String featureDescriptor);
}
