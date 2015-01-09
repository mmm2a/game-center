package com.morgan.shared.nav;

import javax.annotation.Nullable;

/**
 * Interface for a type that represents the format of a place type in the URL bar and can convert
 * between that representation and the place type and vice-versa.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface ApplicationPlaceRepresentation<P extends ApplicationPlace> {

  /**
   * Asks this representation to try and parse the urlToken (the part of the url that is the
   * fragment) into an {@link ApplicationPlace} type.  If this representation can't do that parsing
   * because the fragment token is unrecognized, then it simply returns {@code null}.
   *
   * <p>For a URL like {@code http://tempuri.org/?p=foo&q=bar#!alpha/beta}, the token part is
   * {@code !alpha/beta}, <b>not</b> {@code alpha/beta} and <b>not</b> {@code #!alpha/beta}.
   */
  @Nullable P parsePlaceFromToken(String urlToken);

  /**
   * Generates the URL token fragment.  This will be everything AFTER the fragment character (#),
   * but might include (if appropriate for the place) the ! character that is often found there.
   */
  String generateUrlTokenFor(P place);
}
