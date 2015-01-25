package com.morgan.shared.auth;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

/**
 * Tests for the {@link AuthApplicationPlaceRepresentation} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AuthApplicationPlaceRepresentationTest {

  private AuthApplicationPlaceRepresentation representation;

  @Before public void createTestInstances() {
    representation = new AuthApplicationPlaceRepresentation();
  }

  @Test public void parseFromPartsAfterToken_notEmpty_returnsNull() {
    assertThat(representation.parseFromPartsAfterToken(ImmutableList.of("one"), null)).isNull();
  }

  @Test public void parseFromPartsAfterToken_empty_returnsLoginPlace() {
    assertThat(representation.parseFromPartsAfterToken(ImmutableList.<String>of(), null))
        .isEqualTo(new AuthApplicationPlace());
  }

  @Test public void getPathPartsAfterTokenFor() {
    assertThat(representation.getPathPartsAfterTokenFor(new AuthApplicationPlace())).isEmpty();
  }
}
