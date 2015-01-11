package com.morgan.client.nav;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.morgan.shared.nav.ApplicationPlace;

/**
 * Tests for the {@link PlaceRepresentationHelper} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class PlaceRepresentationHelperTest {

  @Mock private ApplicationPlace mockPlace;

  private PlaceRepresentationHelper helper;

  @Before public void createTestInstances() {
    helper = new PlaceRepresentationHelper();
  }

  @Test public void parseFromHistoryToken() {
    // Place holder until we implement this class.
    assertThat(helper.parseFromHistoryToken("history token")).isNull();
  }

  @Test public void representPlaceAsHistoryToken() {
    // Place holder until we implement this class.
    assertThat(helper.representPlaceAsHistoryToken(mockPlace)).isNull();
  }
}
