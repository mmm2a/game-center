package com.morgan.server.feature;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.CharSource;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * A class representing a set of features configured in a configuration file.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class FeatureConfiguration {

  private static final Pattern FEATURE_CONFIGURATION_PATTERN = Pattern.compile(
      "^\\s*(\\w+)\\s*\\(([^\\)]*)\\)\\s*$");

  private static final Splitter FEATURE_EXPRESSION_SPLITTER =
      Splitter.on('|').omitEmptyStrings().trimResults();

  private final ImmutableSet<ServerFeatureEvaluator> evaluators;
  private final ImmutableMap<String, ImmutableList<String>> featureNameToListOfExpressionsMap;

  @AssistedInject FeatureConfiguration(
      Set<ServerFeatureEvaluator> evaluators,
      @Assisted CharSource fileSource) {
    this.evaluators = ImmutableSet.copyOf(evaluators);

    ImmutableMap.Builder<String, ImmutableList<String>> mapBuilder = ImmutableMap.builder();

    try {
      ImmutableList<String> lines = fileSource.readLines();
      for (int i = 0; i < lines.size(); i++) {
        String line = lines.get(i);
        int comment = line.indexOf('#');
        if (comment >= 0) {
          line = line.substring(0, comment).trim();
        }
        if (!line.isEmpty()) {
          Matcher matcher = FEATURE_CONFIGURATION_PATTERN.matcher(line);
          Preconditions.checkState(matcher.matches(),
              String.format("Unable to parse line %d from %s", i+1, fileSource));
          mapBuilder.put(
              matcher.group(1),
              ImmutableList.copyOf(FEATURE_EXPRESSION_SPLITTER.split(matcher.group(2))));
        }
      }
    } catch (IOException ioe) {
      throw new RuntimeException("Unable to read configuration file: " + fileSource);
    }

    this.featureNameToListOfExpressionsMap = mapBuilder.build();
  }

  /**
   * Checks to see if the given feature is explicitly enabled or disabled in this configuration.
   * Returns {@link Optional#absent()} if it can't determine.
   */
  Optional<Boolean> isEnabled(Enum<?> feature) {
    ImmutableList<String> expressions = featureNameToListOfExpressionsMap.get(feature.name());
    if (expressions != null) {
      for (String expression : expressions) {
        for (ServerFeatureEvaluator evaluator : evaluators) {
          if (evaluator.shouldEnable(expression).or(false)) {
            return Optional.of(true);
          }
        }
      }

      return Optional.of(false);
    }

    return Optional.absent();
  }

  /**
   * GUICE factory interface for creating {@link FeatureConfiguration} instances.
   *
   * @author mark@mark-morgan.net (Mark Morgan)
   */
  interface Factory {
    /**
     * GUICE factory method for creating {@link FeatureConfiguration} instances.
     */
    FeatureConfiguration create(CharSource fileSource);
  }
}
