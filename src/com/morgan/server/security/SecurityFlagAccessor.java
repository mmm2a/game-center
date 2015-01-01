package com.morgan.server.security;

import com.morgan.server.util.flag.Flag;
import com.morgan.server.util.flag.FlagAccessor;

/**
 * A flag accessor for accessing security-related flags.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
interface SecurityFlagAccessor extends FlagAccessor {

  @Flag(name = "keystore",
      description = "The path to the keystore to get security-related keys from",
      required = true)
  String keystorePath();

  @Flag(name = "keystore-passwd",
      description = "The password needed to access the security keystore",
      required = true)
  String keystorePassword();

  @Flag(name = "keystore-type",
      description = "The type for the security keystore",
      required = true)
  String keystoreType();

  @Flag(name = "obfuscator-alias",
      description = "The alias of a symettric key to be used for cookie and name obfuscation",
      required = true)
  String obfuscatorAlias();

  @Flag(name = "obfuscator-passwd",
      description = "The password needed to access the obfuscator symmetric key",
      required = true)
  String obfuscatorPassword();

  @Flag(name = "ssl-alias",
      description = "The alias of a X.509 certificate to use for the SSL socket",
      required = true)
  String sslCertAlias();

  @Flag(name = "ssl-passwd",
      description = "The password used to access the X.509 certificate from the keystore",
      required = true)
  String sslCertPassword();
}
