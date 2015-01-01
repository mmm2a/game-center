package com.morgan.server.feature;

import com.google.common.base.Optional;
import com.google.inject.Inject;

/**
 * An implementation of the {@link ServerFeatureEvaluator} that understands the simple
 * strings {@code enable} and {@code disable} only.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class SimpleEnableDisableEvaluator implements ServerFeatureEvaluator {

  private static final String ENABLE_STRING = "enable";
  private static final String DISABLE_STRING = "disable";

  @Inject SimpleEnableDisableEvaluator() {
  }

  @Override public Optional<Boolean> shouldEnable(String featureDescriptor) {
    switch (featureDescriptor) {
      case ENABLE_STRING :
        return Optional.of(Boolean.TRUE);

      case DISABLE_STRING :
        return Optional.of(Boolean.FALSE);

      default :
        return Optional.<Boolean>absent();
    }
  }
}
