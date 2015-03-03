package com.morgan.server.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.annotation.Nullable;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.Instant;
import org.joda.time.ReadableInstant;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.morgan.server.util.log.AdvancedLogger;
import com.morgan.server.util.time.Clock;

/**
 * Helper class for helping deal with authentication cookies.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class CookieHelper {

  private static final AdvancedLogger LOG = new AdvancedLogger(CookieHelper.class);

  private static final String COOKIE_NAME = "com.morgan.game-center.auth";

  private final SecurityFlagAccessor flagAccessor;
  private final Clock clock;
  private final Provider<HttpServletRequest> requestProvider;
  private final Provider<HttpServletResponse> responseProvider;

  private final AuthenticationCookieObfuscator obfuscator;

  @Inject CookieHelper(
      SecurityFlagAccessor flagAccessor,
      Clock clock,
      Provider<HttpServletRequest> requestProvider,
      Provider<HttpServletResponse> responseProvider,
      AuthenticationCookieObfuscator obfuscator) {
    this.flagAccessor = flagAccessor;
    this.clock = clock;
    this.requestProvider = requestProvider;
    this.responseProvider = responseProvider;
    this.obfuscator = obfuscator;
  }

  @Nullable private Cookie findCookie() {
    Cookie[] cookies = requestProvider.get().getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(COOKIE_NAME)) {
          return cookie;
        }
      }
    }

    return null;
  }

  private boolean isValid(AuthenticationCookie cookie) {
    return cookie.getCookieVersion().equals(flagAccessor.cookieVersion())
        && cookie.getRemoteAddr().equalsIgnoreCase(requestProvider.get().getRemoteAddr())
        && cookie.getValidUntil().isAfter(clock.now());
  }

  private AuthenticationCookie cookieFromBytes(byte[] bytes) {
    ByteArrayInputStream bIn = new ByteArrayInputStream(bytes);
    try {
      ObjectInputStream ois = new ObjectInputStream(bIn);
      return new AuthenticationCookie(
          ois.readLong(),
          ois.readUTF(),
          new Instant(ois.readLong()),
          ois.readUTF());
    } catch (IOException ioe) {
      // If anything happens, log it, but simply say there isn't a cookie
      LOG.debug(ioe, "Error trying to deserialize cookie from bytes");
      return AuthenticationCookie.INVALID_COOKIE;
    }
  }

  private byte[] cookieToBytes(AuthenticationCookie cookie) throws IOException {
    ByteArrayOutputStream bOut = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bOut);
    oos.writeLong(cookie.getUserId());
    oos.writeUTF(cookie.getRemoteAddr());
    oos.writeLong(cookie.getValidUntil().getMillis());
    oos.writeUTF(cookie.getCookieVersion());
    oos.close();
    return bOut.toByteArray();
  }

  private void setAuthenticationCookieIntoResponse(AuthenticationCookie authCookie) {
    try {
      String cookieValue = obfuscator.obfuscateAuthenticationCookie(cookieToBytes(authCookie));
      Cookie cookie = new Cookie(COOKIE_NAME, cookieValue);
      cookie.setPath("/");
      cookie.setMaxAge((int) (flagAccessor.cookieValidationDuration().getMillis() / 1000));
      responseProvider.get().addCookie(cookie);
    } catch (Exception e) {
      // If anything goes wrong, we obviously fail to set the cookie.  We throw an exception to
      // higher levels to deal with this
      LOG.warning(e, "Unable to set the authentication cookie into the header for user %d",
          authCookie.getUserId());
      throw new RuntimeException("Unable to set authentication cookie into header.", e);
    }
  }

  /**
   * Gets the user id from the current session cookies.  If the current session cookies don't exist,
   * or the cookies have expired, then {@link Optional#absent()} is returned.
   */
  public Optional<Long> getUserIdFromCookie() {
    Optional<Long> userId = Optional.absent();

    Cookie cookie = findCookie();
    if (cookie != null) {
      String encrypted = cookie.getValue();
      try {
        AuthenticationCookie authCookie = cookieFromBytes(obfuscator.deobfuscateId(encrypted));
        if (isValid(authCookie)) {
          return Optional.of(authCookie.getUserId());
        }
      } catch (Exception e) {
        LOG.debug(e, "Error trying to de-obfuscate the authentication cookie for a user");
      }
    }

    return userId;
  }

  /**
   * Creates and sets the cookie into the current {@link HttpServletResponse} to identify the
   * given user.
   */
  public void setAuthenticationCookieFor(long userId) {
    invalidateCurrentCookie();
    setAuthenticationCookieIntoResponse(new AuthenticationCookie(
        userId,
        requestProvider.get().getRemoteAddr(),
        clock.now().toInstant().plus(flagAccessor.cookieValidationDuration()),
        flagAccessor.cookieVersion()));
  }

  /**
   * Invalidates the current cookie (simply by putting in the invalid cookie object.  This is
   * basically equivalent to loggging out.
   */
  public void invalidateCurrentCookie() {
    Cookie cookie = findCookie();
    if (cookie != null) {
      cookie.setMaxAge(0);
    }
  }

  /**
   * Logs the current user out.
   */
  public void logout() {
    invalidateCurrentCookie();
    setAuthenticationCookieIntoResponse(AuthenticationCookie.INVALID_COOKIE);
  }

  /**
   * Data class representation of an authentication cookie.
   *
   * @author mark@mark-morgan.net (Mark Morgan)
   */
  private static final class AuthenticationCookie {

    static final AuthenticationCookie INVALID_COOKIE =
        new AuthenticationCookie(-1L, "no address", new Instant(0), "invalid version");

    private final long userId;
    private final String remoteAddr;
    private final ReadableInstant validUntil;
    private final String cookieVersion;

    AuthenticationCookie(
        long userId,
        String remoteAddr,
        ReadableInstant validUntil,
        String cookieVersion) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(remoteAddr));
      Preconditions.checkArgument(!Strings.isNullOrEmpty(cookieVersion));
      this.userId = userId;
      this.remoteAddr = remoteAddr;
      this.validUntil = validUntil;
      this.cookieVersion = cookieVersion;
    }

    long getUserId() {
      return userId;
    }

    String getRemoteAddr() {
      return remoteAddr;
    }

    ReadableInstant getValidUntil() {
      return validUntil;
    }

    String getCookieVersion() {
      return cookieVersion;
    }
  }
}
