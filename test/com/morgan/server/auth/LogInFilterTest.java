package com.morgan.server.auth;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Optional;
import com.morgan.server.security.CookieHelper;
import com.morgan.server.util.time.fake.FakeClock;

/**
 * Tests for the {@link LogInFilter} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class LogInFilterTest {

  private static final ReadableDuration PRE_AUTH_EXPIRE_DURATION = Duration.standardDays(1);

  @Mock private AuthFlagAccessor mockFlags;
  @Mock private CookieHelper mockCookieHelper;

  @Mock private HttpServletRequest mockRequest;
  @Mock private HttpServletResponse mockResponse;
  @Mock private FilterChain mockFilterChain;

  private FakeClock clock;

  private LogInFilter filter;

  @Before public void createTestInstances() {
    when(mockFlags.preAuthExpireRedirectInterval()).thenReturn(PRE_AUTH_EXPIRE_DURATION);

    clock = new FakeClock();
    filter = new LogInFilter(clock, mockFlags, mockCookieHelper);
  }

  @Test public void doFilter_notLoggedIn_doesRedirect() throws Exception {
    when(mockCookieHelper.getCookieExpirationTime()).thenReturn(Optional.<ReadableInstant>absent());
    filter.doFilter(mockRequest, mockResponse, mockFilterChain);
    verify(mockResponse).sendRedirect("/apps/auth#!authenticate");
    verifyZeroInteractions(mockFilterChain);
  }

  @Test public void doFilter_loggedInButExpiringSoon_doesRedirect() throws Exception {
    ReadableInstant now = new Instant();
    clock.setTime(now);
    ReadableInstant expirationTime = now.toInstant().plus(Duration.standardHours(5));

    when(mockCookieHelper.getCookieExpirationTime()).thenReturn(Optional.of(expirationTime));
    filter.doFilter(mockRequest, mockResponse, mockFilterChain);
    verify(mockResponse).sendRedirect("/apps/auth#!authenticate");
    verifyZeroInteractions(mockFilterChain);
  }

  @Test public void doFilter_loggedInAndValidForAWhile_continuesToChain() throws Exception {
    ReadableInstant now = new Instant();
    clock.setTime(now);
    ReadableInstant expirationTime = now.toInstant().plus(Duration.standardDays(5));

    when(mockCookieHelper.getCookieExpirationTime()).thenReturn(Optional.of(expirationTime));
    filter.doFilter(mockRequest, mockResponse, mockFilterChain);
    verify(mockFilterChain).doFilter(mockRequest, mockResponse);
    verifyZeroInteractions(mockResponse);
  }
}
