package com.morgan.shared.nav;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Abstract implementation of the {@link ApplicationPlaceRepresentation} interface that simplifies
 * common tasks for a common variety of place.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public abstract class AbstractApplicationPlaceRepresentation<P extends ApplicationPlace>
    implements ApplicationPlaceRepresentation<P> {

  private static final String TOKEN_PREFIX = "!";
  private static final String TOKEN_SEPARATOR = "/";
  private static final String PARAMETER_START = "(";
  private static final String PARAMETER_END = ")";
  private static final String PARAMETER_EQUALS = "=";
  private static final String PARAMETER_SEPARATOR = ",";

  protected AbstractApplicationPlaceRepresentation() {
  }

  /**
   * Asks the class to create the {@link ApplicationPlace} from the given input parts.  The derived
   * implementation is still welcome to return {@code null} indicating that the place was not
   * understood.
   */
  @Nullable protected abstract P parseFromParts(
      ImmutableList<String> pathParts,
      ImmutableMap<String, String> parameterMap);

  /**
   * Get the parameter map for the input place instance.
   */
  protected abstract Map<String, String> getParametersFor(P place);

  /**
   * Gets the path parts for the given input place.
   */
  protected abstract List<String> getPathPartsFor(P place);

  @Override public final P parsePlaceFromToken(String urlToken) {
    Preconditions.checkNotNull(urlToken);
    if (urlToken.startsWith(TOKEN_PREFIX)) {

    }
  }

  @Override public final String generateUrlTokenFor(P place) {
  }
}
