package com.morgan.client.nav;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.morgan.shared.nav.ApplicationPlace;
import com.morgan.shared.nav.ClientApplication;
import com.morgan.shared.nav.NavigationConstant;
import com.morgan.testing.FakeClientPageConstants;

/**
 * Tests for the {@link UrlCreator} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class UrlCreatorTest {

  private static final ClientApplication CURRENT_APPLICATION = ClientApplication.AUTHENTICATION;

  @Mock private PlaceRepresentationHelper mockRepresentationHelper;
  @Mock private ApplicationPlace mockTargetPlace;

  private FakeClientPageConstants constants;

  private UrlCreator urlCreator;

  @Before public void createTestInstances() {
    constants = new FakeClientPageConstants();
    urlCreator = new UrlCreator(constants, CURRENT_APPLICATION, mockRepresentationHelper);
  }

  @Test public void createUrlFor() {
    when(mockRepresentationHelper.representPlaceAsHistoryToken(mockTargetPlace))
        .thenReturn("!some-token");
    constants.setValue(NavigationConstant.APPLICATION_URL, "some-url/apps/auth");
    when(mockTargetPlace.getClientApplication()).thenReturn(ClientApplication.GAME_SERVER);
    assertThat(urlCreator.createUrlFor(mockTargetPlace))
        .isEqualTo("some-url/apps/game#!some-token");
  }
}
