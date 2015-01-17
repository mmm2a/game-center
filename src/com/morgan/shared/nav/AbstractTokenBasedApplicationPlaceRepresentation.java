package com.morgan.shared.nav;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Abstract implementatino of an {@link ApplicationPlaceRepresentation} that understands that the
 * first part of the path that it needs is the token.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public abstract class AbstractTokenBasedApplicationPlaceRepresentation
    extends AbstractApplicationPlaceRepresentation {

  private final String expectedToken;

  protected AbstractTokenBasedApplicationPlaceRepresentation(String expectedToken) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(expectedToken));
    this.expectedToken = expectedToken;
  }

  protected abstract ApplicationPlace parseFromPartsAfterToken(
      ImmutableList<String> remainingParts, ImmutableMap<String, String> parameterMap);

  protected abstract Iterable<String> getPathPartsAfterTokenFor(ApplicationPlace place);

  @Override protected final @Nullable ApplicationPlace parseFromParts(
      ImmutableList<String> pathParts,
      ImmutableMap<String, String> parameterMap) {
    if (pathParts.isEmpty() || !pathParts.get(0).equals(expectedToken)) {
      return null;
    }

    return parseFromPartsAfterToken(pathParts.subList(1, pathParts.size()), parameterMap);
  }

  @Override protected final List<String> getPathPartsFor(ApplicationPlace place) {
    return ImmutableList.<String>builder()
        .add(expectedToken)
        .addAll(getPathPartsAfterTokenFor(place))
        .build();
  }
}
