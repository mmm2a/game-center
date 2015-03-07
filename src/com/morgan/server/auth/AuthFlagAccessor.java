package com.morgan.server.auth;

import org.joda.time.ReadableDuration;

import com.morgan.server.util.flag.Flag;
import com.morgan.server.util.flag.FlagAccessor;
import com.morgan.server.util.time.DurationFlagParser;

/**
 * A flag accessor for accessing authentication-releated flags.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
interface AuthFlagAccessor extends FlagAccessor {

  @Flag(name = "auth-pre-expire-redirect-interval",
      description = "An interval of time before the expiration timestamp on authenticated "
          + "credentials when the login redirector will start redirecting page loads",
      defaultValue = "1d",
      required = false,
      parser = DurationFlagParser.class)
  ReadableDuration preAuthExpireRedirectInterval();
}
