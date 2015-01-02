package com.morgan.server.security;

import org.joda.time.ReadableDuration;

import com.morgan.server.util.flag.Flag;
import com.morgan.server.util.flag.FlagAccessor;
import com.morgan.server.util.time.DurationFlagParser;

/**
 * A flag accessor for accessing security-related flags.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
interface SecurityFlagAccessor extends FlagAccessor {

  @Flag(name = "obfuscation-keystore",
      description = "The path to the keystore to get security-related keys from",
      required = true)
  String obfuscationKeystorePath();

  @Flag(name = "obfuscation-keystore-passwd",
      description = "The password needed to access the security keystore",
      required = true)
  String obfuscationKeystorePassword();

  @Flag(name = "obfuscation-keystore-type",
      description = "The type for the security keystore",
      required = true)
  String obfuscationKeystoreType();

  @Flag(name = "obfuscator-alias",
      description = "The alias of a symettric key to be used for cookie and name obfuscation",
      required = true)
  String obfuscatorAlias();

  @Flag(name = "obfuscator-passwd",
      description = "The password needed to access the obfuscator symmetric key",
      required = true)
  String obfuscatorPassword();

  @Flag(name = "ssl-keystore",
      description = "The path to the keystore to get security-related keys from",
      required = true)
  String sslKeystorePath();

  @Flag(name = "ssl-keystore-passwd",
      description = "The password needed to access the security keystore",
      required = true)
  String sslKeystorePassword();

  @Flag(name = "ssl-keystore-type",
      description = "The type for the security keystore",
      required = true)
  String sslKeystoreType();

  @Flag(name = "ssl-alias",
      description = "The alias of a X.509 certificate to use for the SSL socket",
      required = true)
  String sslCertAlias();

  @Flag(name = "ssl-passwd",
      description = "The password used to access the X.509 certificate from the keystore",
      required = true)
  String sslCertPassword();

  @Flag(name = "cookie-validation-duration",
      description = "Indicates the duration of time that cookies, once created, are valid for",
      required = false,
      defaultValue = "30d",
      parser = DurationFlagParser.class)
  ReadableDuration cookieValidationDuration();

  @Flag(name = "cookie-version",
      description = "A version string indicating the cookie version",
      required = false,
      defaultValue = "2015-01-01")
  String cookieVersion();
}
