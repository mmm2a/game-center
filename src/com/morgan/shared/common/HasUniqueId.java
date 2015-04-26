package com.morgan.shared.common;

/**
 * Interface for a type that can contain a unique ID.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface HasUniqueId {

  /**
   * Retrieves this instance's unique ID.
   */
  long getId();
}
