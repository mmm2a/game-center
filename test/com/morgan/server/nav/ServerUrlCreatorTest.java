package com.morgan.server.nav;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.util.Providers;
import com.morgan.shared.auth.AuthApplicationPlace;

/**
 * Tests for the {@link ServerUrlCreator} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class ServerUrlCreatorTest {

  @Mock private HttpServletRequest mockRequest;

  private ServerUrlCreator creator;

  @Before public void createTestInstances() {
    creator = new ServerUrlCreator(Providers.of(mockRequest));
  }

  @Before public void setUpCommonMockInteractions() {
    when(mockRequest.getRequestURL()).thenReturn(
        new StringBuffer("https://my.host.com:1234/something-wrong?foo=bar"));
  }

  @Test public void createUrlFor() {
    assertThat(creator.createUrlFor(new AuthApplicationPlace()).asString())
        .isEqualTo("https://my.host.com:1234/apps/auth/#!authenticate");
  }
}
