package com.morgan.server.feature;

import java.io.File;
import java.nio.charset.Charset;

import javax.annotation.Nullable;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.morgan.shared.feature.FeatureChecker;

/**
 * Implementation of a {@link FeatureChecker} that is used on the server-side of an application.
 * This feature checker works by looking for features defined in a few places (in the following
 * precedence):
 * <ul>
 *   <li>Features can be explicitly disabled on the command line with the --disable=f1,f2,f3 flag
 *       and enabled on the command line with the --enable=f1,f2,f3 flag.
 *   <li>Features can be explicitly enabled/disabled by a configuration file identified by the
 *       --features-file=<file> flag.
 *   <li>Features can be explicitly enabled/disabled by a features configuration file located in
 *       the META-INF/features.conf resource.
 *   <li>All other features are always enabled.
 * </ul>
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class ServerFeatureChecker<T extends Enum<T>> implements FeatureChecker<T> {

  private static final String DEFAULT_FEATURES_CONF_RESOURCE_PATH =
      "/META-INF/features.conf";

  private final ImmutableSet<String> commandLineDisabledFeatures;
  private final ImmutableSet<String> commandLineEnabledFeatures;

  @Nullable private final FeatureConfiguration commandLineConfiguredConfiguration;
  private final FeatureConfiguration defaultConfiguration;

  @Inject ServerFeatureChecker(
      FeatureFlagAccessor featureFlagAccessor,
      FeatureConfiguration.Factory configurationFactory) {
    commandLineEnabledFeatures = featureFlagAccessor.enablements();
    commandLineDisabledFeatures = featureFlagAccessor.disablements();

    String featuresFile = featureFlagAccessor.featuresFile();
    if (featuresFile == null) {
      commandLineConfiguredConfiguration = null;
    } else {
      File file = new File(featuresFile);
      Preconditions.checkState(file.canRead(), "Can't read file %s", featuresFile);
      commandLineConfiguredConfiguration = configurationFactory.create(
          Files.asCharSource(file, Charset.defaultCharset()));
    }

    defaultConfiguration = configurationFactory.create(Resources.asCharSource(
        Resources.getResource(DEFAULT_FEATURES_CONF_RESOURCE_PATH), Charset.defaultCharset()));
  }

  private Optional<Boolean> checkCommandLineFor(T feature) {
    if (commandLineDisabledFeatures.contains(feature.name())) {
      return Optional.of(false);
    }

    if (commandLineEnabledFeatures.contains(feature.name())) {
      return Optional.of(true);
    }

    return Optional.absent();
  }

  private Optional<Boolean> checkCommandLineConfiguredFlagsFor(T feature) {
    if (commandLineConfiguredConfiguration != null) {
      return commandLineConfiguredConfiguration.isEnabled(feature);
    }

    return Optional.absent();
  }

  private Optional<Boolean> checkDefaultConfiguredFlagsFor(T feature) {
    return defaultConfiguration.isEnabled(feature);
  }

  @Override public boolean isEnabled(T feature) {
    Optional<Boolean> answer = checkCommandLineFor(feature);
    if (!answer.isPresent()) {
      answer = checkCommandLineConfiguredFlagsFor(feature);
      if (!answer.isPresent()) {
        answer = checkDefaultConfiguredFlagsFor(feature);
      }
    }

    return answer.or(true);
  }
}
