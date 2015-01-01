package com.morgan.server.security;

import com.google.inject.ImplementedBy;

/**
 * An interface for a type that can obfuscate (encrypt) and de-obfuscate (decrypt) authentication
 * cookies.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@ImplementedBy(DefaultAuthenticationCookieObfuscator.class)
interface AuthenticationCookieObfuscator {

  /**
   * Obfuscate an authentication cookie
   */
  String obfuscateAuthenticationCookie(byte[] clear);

  /**
   * Deobfuscate an authentication cookie
   */
  byte[] deobfuscateId(String encrypted);
}
