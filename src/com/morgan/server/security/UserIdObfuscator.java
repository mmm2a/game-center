package com.morgan.server.security;

import com.google.inject.ImplementedBy;

/**
 * Type that can obfuscate and deobfuscate user ids.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@ImplementedBy(DefaultUserIdObfuscator.class)
public interface UserIdObfuscator {

  /**
   * Obfuscate a user id into a string representation.
   */
  String obfuscateId(long id);

  /**
   * Deobfuscate a user id from a string representation.
   */
  long deobfuscateId(String id);
}
