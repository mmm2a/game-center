package com.morgan.server.nav;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.util.Providers;
import com.morgan.server.constants.PageConstants;
import com.morgan.shared.nav.NavigationConstant;

/**
 * Tests for the {@link NavigationPageConstantsSource} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class NavigationPageConstantsSourceTest {

  @Mock private HttpServletRequest mockRequest;
  @Mock private PageConstants mockConstantsSink;

  private NavigationPageConstantsSource source;

  @Before public void createTestInstances() {
    source = new NavigationPageConstantsSource(Providers.of(mockRequest));
  }

  private void doProvideConstantsIntoTest(
      String url, String host, int port, String expectedProtocol) {
    when(mockRequest.getRequestURL()).thenReturn(new StringBuffer(url));
    when(mockRequest.getServerName()).thenReturn(host);
    when(mockRequest.getServerPort()).thenReturn(port);

    source.provideConstantsInto(mockConstantsSink);

    verify(mockConstantsSink).add(NavigationConstant.APPLICATION_URL, url);
    verify(mockConstantsSink).add(NavigationConstant.APPLICATION_HOST, host);
    verify(mockConstantsSink).add(NavigationConstant.APPLICATION_PORT, port);
    verify(mockConstantsSink).add(NavigationConstant.APPLICATION_PROTOCOL, expectedProtocol);
  }

  @Test public void provideConstantsInto_http() {
    doProvideConstantsIntoTest("http://localhost:8080/apps/foo", "localhost", 8080, "http");
  }

  @Test public void provideConstantsInto_https() {
    doProvideConstantsIntoTest("https://localhost:8080/apps/foo", "localhost", 8080, "https");
  }
}
