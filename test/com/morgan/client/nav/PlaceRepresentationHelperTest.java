package com.morgan.client.nav;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableSet;
import com.morgan.shared.nav.ApplicationPlace;
import com.morgan.shared.nav.ApplicationPlaceRepresentation;

/**
 * Tests for the {@link PlaceRepresentationHelper} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class PlaceRepresentationHelperTest {

  @Mock private ApplicationPlace mockDefaultPlace;
  @Mock private ApplicationPlace mockOtherPlace;

  @Mock private ApplicationPlaceRepresentation mockRepresentation1;
  @Mock private ApplicationPlaceRepresentation mockRepresentation2;
  @Mock private ApplicationPlaceRepresentation mockRepresentation3;

  private PlaceRepresentationHelper helper;

  @Before public void createTestInstances() {
    helper = new PlaceRepresentationHelper(
        mockDefaultPlace,
        ImmutableSet.of(mockRepresentation1, mockRepresentation2, mockRepresentation3));
  }

  @Test public void parseFromHistoryToken_noMatches_returnsDefault() {
    assertThat(helper.parseFromHistoryToken("any string")).isEqualTo(mockDefaultPlace);
  }

  @Test public void parseFromHistoryToken_matchReturned() {
    when(mockRepresentation2.parsePlaceFromToken("history token")).thenReturn(mockOtherPlace);
    assertThat(helper.parseFromHistoryToken("history token")).isEqualTo(mockOtherPlace);
  }

  @Test public void representPlaceAsHistoryToken() {
    when(mockOtherPlace.getRepresentation()).thenReturn(mockRepresentation3);
    when(mockRepresentation3.generateUrlTokenFor(mockOtherPlace)).thenReturn("result");
    assertThat(helper.representPlaceAsHistoryToken(mockOtherPlace)).isEqualTo("result");
  }
}
