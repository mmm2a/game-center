package com.morgan.shared.feature;

/**
 * An interface for a type that can check whether or not a given feature is enabled.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface FeatureChecker<T extends Enum<T>> {

  /**
   * Indicates whether or not the given feature is enabled.
   */
  boolean isEnabled(T feature);
}
