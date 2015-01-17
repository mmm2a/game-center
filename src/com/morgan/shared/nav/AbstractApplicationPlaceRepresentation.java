package com.morgan.shared.nav;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.base.Splitter.MapSplitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

/**
 * Abstract implementation of the {@link ApplicationPlaceRepresentation} interface that simplifies
 * common tasks for a common variety of place.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public abstract class AbstractApplicationPlaceRepresentation
    implements ApplicationPlaceRepresentation {

  private static final String TOKEN_PREFIX = "!";
  private static final String TOKEN_SEPARATOR = "/";
  private static final String PARAMETER_START = "(";
  private static final String PARAMETER_END = ")";
  private static final String PARAMETER_EQUALS = "=";
  private static final String PARAMETER_SEPARATOR = ",";

  private static final CharMatcher INVALID_PATH_CHAR_MATCHER = CharMatcher.anyOf(
      TOKEN_SEPARATOR + PARAMETER_START + PARAMETER_END);
  private static final CharMatcher INVALID_PARAMETER_CHAR_MATCHER = CharMatcher.anyOf(
      PARAMETER_EQUALS + PARAMETER_SEPARATOR);

  private static final Splitter PATH_SPLITTER = Splitter.on(TOKEN_SEPARATOR).omitEmptyStrings();
  private static final Joiner PATH_JOINER = Joiner.on(TOKEN_SEPARATOR);

  private static final Predicate<String> CONTAINS_INVALID_PATH_CHAR =
      new Predicate<String>() {
        @Override public boolean apply(@Nullable String input) {
          return input == null || INVALID_PATH_CHAR_MATCHER.matchesAnyOf(input);
        }
      };

  private static final MapSplitter PARAMETER_SPLITTER = Splitter.on(PARAMETER_SEPARATOR)
      .omitEmptyStrings()
      .trimResults()
      .withKeyValueSeparator(PARAMETER_EQUALS);
  private static final MapJoiner PARAMETER_JOINER = Joiner.on(PARAMETER_SEPARATOR)
      .withKeyValueSeparator(PARAMETER_EQUALS);

  protected AbstractApplicationPlaceRepresentation() {
  }

  /**
   * Asks the class to create the {@link ApplicationPlace} from the given input parts.  The derived
   * implementation is still welcome to return {@code null} indicating that the place was not
   * understood.
   */
  @Nullable protected abstract ApplicationPlace parseFromParts(
      ImmutableList<String> pathParts,
      ImmutableMap<String, String> parameterMap);

  /**
   * Get the parameter map for the input place instance.
   */
  protected final Map<String, String> getParametersFor(ApplicationPlace place) {
    return place.getParameters();
  }

  /**
   * Gets the path parts for the given input place.
   */
  protected abstract List<String> getPathPartsFor(ApplicationPlace place);

  @Override public final ApplicationPlace parsePlaceFromToken(String urlToken) {
    ApplicationPlace place = null;

    Preconditions.checkNotNull(urlToken);
    if (urlToken.startsWith(TOKEN_PREFIX)) {
      urlToken = urlToken.substring(1);

      int index = urlToken.indexOf(PARAMETER_START);
      if (index == 0) {
        return null;
      }

      ImmutableMap<String, String> parameters;
      if (index > 0) {
        if (!urlToken.endsWith(PARAMETER_END)) {
          return null;
        }
        try {
          parameters = ImmutableMap.copyOf(
              PARAMETER_SPLITTER.split(urlToken.substring(index + 1, urlToken.length() - 1)));
        } catch (IllegalArgumentException e) {
          return null;
        }
        urlToken = urlToken.substring(0, index);
      } else {
        parameters = ImmutableMap.of();
      }

      place = parseFromParts(ImmutableList.copyOf(PATH_SPLITTER.split(urlToken)), parameters);
    }

    return place;
  }

  @Override public final String generateUrlTokenFor(ApplicationPlace place) {
    Preconditions.checkNotNull(place);

    List<String> pathParts = getPathPartsFor(place);
    Map<String, String> parameters = getParametersFor(place);

    Preconditions.checkState(Iterables.isEmpty(
        Iterables.filter(pathParts, CONTAINS_INVALID_PATH_CHAR)));
    for (Map.Entry<String, String> entry : parameters.entrySet()) {
      Preconditions.checkState(!INVALID_PARAMETER_CHAR_MATCHER.matchesAnyOf(entry.getKey()));
      Preconditions.checkState(!INVALID_PARAMETER_CHAR_MATCHER.matchesAnyOf(entry.getValue()));
    }

    StringBuilder tokenBuilder = new StringBuilder(TOKEN_PREFIX);
    PATH_JOINER.appendTo(tokenBuilder, getPathPartsFor(place));
    if (!parameters.isEmpty()) {
      tokenBuilder.append(PARAMETER_START);
      PARAMETER_JOINER.appendTo(tokenBuilder, parameters);
      tokenBuilder.append(PARAMETER_END);
    }

    return tokenBuilder.toString();
  }
}
