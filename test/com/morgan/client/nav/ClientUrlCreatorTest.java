package com.morgan.client.nav;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gwt.safehtml.shared.UriUtils;
import com.morgan.shared.nav.ApplicationPlace;
import com.morgan.shared.nav.ApplicationPlaceRepresentation;
import com.morgan.shared.nav.ClientApplication;
import com.morgan.shared.nav.NavigationConstant;
import com.morgan.testing.FakeClientPageConstants;

/**
 * Tests for the {@link ClientUrlCreator} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientUrlCreatorTest {

  private static final ClientApplication CURRENT_APPLICATION = ClientApplication.AUTHENTICATION;

  @Mock private ApplicationPlace mockTargetPlace;
  @Mock private ApplicationPlaceRepresentation mockRepresentation;

  private FakeClientPageConstants constants;

  private ClientUrlCreator urlCreator;

  @Before public void createTestInstances() {
    constants = new FakeClientPageConstants();
    urlCreator = new ClientUrlCreator(constants, CURRENT_APPLICATION);
  }

  @Test public void createUrlFor() {
    when(mockTargetPlace.getRepresentation()).thenReturn(mockRepresentation);
    when(mockRepresentation.generateUrlTokenFor(mockTargetPlace))
        .thenReturn("!some-token");
    constants.setValue(NavigationConstant.APPLICATION_URL, "some-url/apps/auth");
    when(mockTargetPlace.getClientApplication()).thenReturn(ClientApplication.GAME_SERVER);
    assertThat(urlCreator.createUrlFor(mockTargetPlace))
        .isEqualTo(UriUtils.fromString("some-url/apps/game#!some-token"));
  }
}
