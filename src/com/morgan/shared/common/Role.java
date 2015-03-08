package com.morgan.shared.common;

/**
 * An enumeration describing the roles that a user can have in the game engine.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public enum Role {
  /** Indicates that a user is an administrator for the game engine running */
  ADMIN,

  /** Indicates that a user is a regular member for the game engine running */
  MEMBER,

  /** Indicates that the caller's role is unknown (i.e., not logged in) */
  UNKNOWN;

  /**
   * Returns {@code true} if this role is at least as powerful as the desired role.
   */
  public boolean hasAccessTo(Role desiredRole) {
    return ordinal() <= desiredRole.ordinal();
  }
}
