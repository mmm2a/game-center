package com.morgan.server.util.flag;

import com.google.common.collect.ImmutableList;

/**
 * An interface for a type that can be used to access flags.  This is just used as a marker
 * interface for those types.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface FlagAccessor {

  /**
   * Gets the ordered list of command line arguments that weren't flags.
   */
  ImmutableList<String> getArguments();
}
