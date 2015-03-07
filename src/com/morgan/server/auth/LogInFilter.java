package com.morgan.server.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.Instant;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morgan.server.security.CookieHelper;
import com.morgan.server.util.time.Clock;

/**
 * A {@link Filter} that redirects the user to the authentication page if s/he isn't already logged
 * in.  This filter takes the additional step of redirecting them if they will be logged out any
 * time in the next day.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
public class LogInFilter implements Filter {

  private final Clock clock;
  private final ReadableDuration preValidDuration;
  private final CookieHelper cookieHelper;

  @Inject LogInFilter(
      Clock clock,
      AuthFlagAccessor flags,
      CookieHelper cookieHelper) {
    this.clock = clock;
    this.preValidDuration = flags.preAuthExpireRedirectInterval();
    this.cookieHelper = cookieHelper;
  }

  @Override public void destroy() {
    // Do nothing
  }

  @Override public void doFilter(
      ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {

    Optional<ReadableInstant> validUntil = cookieHelper.getCookieExpirationTime();
    boolean shouldRedirect = clock.now().toInstant().plus(preValidDuration)
        .isAfter(validUntil.or(new Instant(0)));

    if (shouldRedirect) {
      HttpServletResponse resp = (HttpServletResponse) response;
      resp.sendRedirect("/apps/auth#!authenticate");
    } else {
      chain.doFilter(request, response);
    }
  }

  @Override public void init(FilterConfig config) throws ServletException {
    // Do nothing
  }
}
