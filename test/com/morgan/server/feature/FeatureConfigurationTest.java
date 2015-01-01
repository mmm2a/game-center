package com.morgan.server.feature;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.CharSource;
import com.google.common.truth.Truth;

/**
 * Tests for the {@link FeatureConfiguration} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class FeatureConfigurationTest {

  private static enum FeatureType {
    FEATURE1,
    FEATURE2,
    FEATURE3,
    FEATURE4,
  }

  private static final String FEATURE_FILE =
      "# This is a line with nothing but a comment\n"
      + "\n"
      + "FEATURE1(eval:1) # a comment after a line\n"
      + "FEATURE2(random | eval:2)\n"
      + "FEATURE3(eval:3)\n";

  private static final CharSource SOURCE = CharSource.wrap(FEATURE_FILE);

  private FeatureConfiguration configuration;

  @Before public void createTestInstances() {
    configuration = new FeatureConfiguration(ImmutableSet.<ServerFeatureEvaluator>of(
        new FakeEvaluator("eval:1", true),
        new FakeEvaluator("eval:2", true),
        new FakeEvaluator("eval:3", false)), SOURCE);
  }

  @Test public void isEnabled_simpleEnabledExpression() {
    Truth.assertThat(configuration.isEnabled(FeatureType.FEATURE1).get()).isTrue();
  }

  @Test public void isEnabled_complexEnabledExpression() {
    Truth.assertThat(configuration.isEnabled(FeatureType.FEATURE2).get()).isTrue();
  }

  @Test public void isEnabled_explicitlyDisabled() {
    Truth.assertThat(configuration.isEnabled(FeatureType.FEATURE3).get()).isFalse();
  }

  @Test public void isEnabled_unknown() {
    Truth.assertThat(configuration.isEnabled(FeatureType.FEATURE4).isPresent()).isFalse();
  }

  private static class FakeEvaluator implements ServerFeatureEvaluator {
    private final String lookFor;
    private final boolean returnOnFind;

    private FakeEvaluator(String lookFor, boolean returnOnFind) {
      this.lookFor = lookFor;
      this.returnOnFind = returnOnFind;
    }

    @Override public Optional<Boolean> shouldEnable(String featureDescriptor) {
      if (featureDescriptor.equals(lookFor)) {
        return Optional.of(returnOnFind);
      }

      return Optional.absent();
    }
  }
}
