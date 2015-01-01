package com.morgan.server.feature;

import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.CharSource;
import com.google.common.truth.Truth;

/**
 * Tests for the {@link ServerFeatureChecker} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class ServerFeatureCheckerTest {

  private static enum FeatureType {
    FEATURE1,
    FEATURE2,
    FEATURE3,
  }

  private static final String FEATURE_FILE = "feature-file";

  @Mock private FeatureFlagAccessor mockFlagAccessor;
  @Mock private FeatureConfiguration.Factory mockConfigurationFactory;
  @Mock private FeatureConfiguration mockConfiguration1;
  @Mock private FeatureConfiguration mockConfiguration2;

  @Mock private CharSource mockCmdFileCharSource;
  @Mock private CharSource mockResourceCharSource;

  private ServerFeatureChecker<FeatureType> createFeatureChecker(
      ImmutableSet<String> cmdEnablements,
      ImmutableSet<String> cmdDisablements,
      boolean hasCmdFeatureFile) {

    when(mockFlagAccessor.enablements()).thenReturn(cmdEnablements);
    when(mockFlagAccessor.disablements()).thenReturn(cmdDisablements);

    when(mockConfigurationFactory.create(mockCmdFileCharSource)).thenReturn(mockConfiguration1);
    when(mockConfigurationFactory.create(mockResourceCharSource)).thenReturn(mockConfiguration2);

    if (hasCmdFeatureFile) {
      when(mockFlagAccessor.featuresFile()).thenReturn(FEATURE_FILE);
    }

    return new TestableServerFeatureChecker();
  }

  @Test public void construction_noExceptionWhenNoCmdFeatureFile() {
    createFeatureChecker(ImmutableSet.<String>of(), ImmutableSet.<String>of(), false);
  }

  @Test public void construction_noExceptionWhenYesCmdFeatureFile() {
    createFeatureChecker(ImmutableSet.<String>of(), ImmutableSet.<String>of(), true);
  }

  @Test public void isEnabled_cmdDisablementsOverrideAll() {
    when(mockConfiguration1.isEnabled(FeatureType.FEATURE1)).thenReturn(Optional.of(false));
    when(mockConfiguration2.isEnabled(FeatureType.FEATURE1)).thenReturn(Optional.of(false));
    Truth.assertThat(createFeatureChecker(
        ImmutableSet.of("FEATURE1"),
        ImmutableSet.of("FEATURE1"),
        true).isEnabled(FeatureType.FEATURE1))
        .isFalse();
  }

  @Test public void isEnabled_cmdEnablementsOverrideConfigurations() {
    when(mockConfiguration1.isEnabled(FeatureType.FEATURE1)).thenReturn(Optional.of(false));
    when(mockConfiguration2.isEnabled(FeatureType.FEATURE1)).thenReturn(Optional.of(false));
    Truth.assertThat(createFeatureChecker(
        ImmutableSet.of("FEATURE1"),
        ImmutableSet.<String>of(),
        true).isEnabled(FeatureType.FEATURE1))
        .isTrue();
  }

  @Test public void isEnabled_cmdFileOverridesDefaultFile() {
    when(mockConfiguration1.isEnabled(FeatureType.FEATURE1)).thenReturn(Optional.of(false));
    when(mockConfiguration2.isEnabled(FeatureType.FEATURE1)).thenReturn(Optional.of(true));
    Truth.assertThat(createFeatureChecker(
        ImmutableSet.<String>of(),
        ImmutableSet.<String>of(),
        true).isEnabled(FeatureType.FEATURE1))
        .isFalse();
  }

  @Test public void isEnabled_complexArrangements() {
    when(mockConfiguration1.isEnabled(FeatureType.FEATURE1)).thenReturn(Optional.of(true));
    when(mockConfiguration1.isEnabled(FeatureType.FEATURE2)).thenReturn(Optional.of(false));
    when(mockConfiguration1.isEnabled(FeatureType.FEATURE3)).thenReturn(Optional.<Boolean>absent());

    when(mockConfiguration2.isEnabled(FeatureType.FEATURE3)).thenReturn(Optional.of(true));

    ServerFeatureChecker<FeatureType> checker = createFeatureChecker(
        ImmutableSet.<String>of(),
        ImmutableSet.<String>of(),
        true);

    Truth.assertThat(checker.isEnabled(FeatureType.FEATURE1)).isTrue();
    Truth.assertThat(checker.isEnabled(FeatureType.FEATURE2)).isFalse();
    Truth.assertThat(checker.isEnabled(FeatureType.FEATURE3)).isTrue();
  }

  @Test public void isEnabled_defaultsToTrue() {
    when(mockConfiguration1.isEnabled(FeatureType.FEATURE1)).thenReturn(Optional.<Boolean>absent());
    when(mockConfiguration2.isEnabled(FeatureType.FEATURE1)).thenReturn(Optional.<Boolean>absent());

    Truth.assertThat(createFeatureChecker(
        ImmutableSet.<String>of(),
        ImmutableSet.<String>of(),
        true).isEnabled(FeatureType.FEATURE1)).isTrue();

    Truth.assertThat(createFeatureChecker(
        ImmutableSet.<String>of(),
        ImmutableSet.<String>of(),
        false).isEnabled(FeatureType.FEATURE1)).isTrue();
  }

  private class TestableServerFeatureChecker extends ServerFeatureChecker<FeatureType> {

    TestableServerFeatureChecker() {
      super(mockFlagAccessor, mockConfigurationFactory);
    }

    @Override CharSource getCharSourceForPath(String filePath) {
      Preconditions.checkArgument(filePath.equals(FEATURE_FILE));
      return mockCmdFileCharSource;
    }

    @Override CharSource getCharSourceForResource(String resource) {
      Preconditions.checkArgument(resource.equals(
          ServerFeatureChecker.DEFAULT_FEATURES_CONF_RESOURCE_PATH));
      return mockResourceCharSource;
    }
  }
}
