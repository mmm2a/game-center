package com.morgan.shared.feature.fake;

import java.util.HashSet;
import java.util.Set;

import com.morgan.shared.feature.FeatureChecker;

/**
 * Fake implementation of a {@link FeatureChecker} that can be used for testing purposes.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class FakeFeatureChecker<T extends Enum<T>> implements FeatureChecker<T> {

  private final Set<T> explicitlyEnabledFeatures = new HashSet<>();

  private boolean allEnabled = true;

  public FakeFeatureChecker() {
  }

  public FakeFeatureChecker<T> enableAll() {
    allEnabled = true;
    explicitlyEnabledFeatures.clear();
    return this;
  }

  public FakeFeatureChecker<T> disableAll() {
    allEnabled = false;
    explicitlyEnabledFeatures.clear();
    return this;
  }

  public FakeFeatureChecker<T> enable(T feature) {
    explicitlyEnabledFeatures.add(feature);
    return this;
  }

  public FakeFeatureChecker<T> disable(T feature) {
    explicitlyEnabledFeatures.remove(feature);
    allEnabled = false;
    return this;
  }

  @Override public boolean isEnabled(T feature) {
    return allEnabled || explicitlyEnabledFeatures.contains(feature);
  }
}
