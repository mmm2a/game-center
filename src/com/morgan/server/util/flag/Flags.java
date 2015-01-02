package com.morgan.server.util.flag;

import javax.annotation.Nullable;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.morgan.server.util.cmdline.CommandLine;

/**
 * Global class for maintaining and manipulating flags for a binary.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class Flags {

  private static Flags instance = null;

  private final CommandLine commandLine;

  private Flags(CommandLine commandLine) {
    this.commandLine = Preconditions.checkNotNull(commandLine);
  }

  /**
   * Gets the string representation for the given flag name.  If the flag was not set, this
   * method returns {@code null}.
   */
  @Nullable public String getStringRepresentationFor(String flagName) {
    return commandLine.getFlagMap().get(flagName);
  }

  /**
   * Gets the list of arguments that were given to the program.
   */
  public ImmutableList<String> getArguments() {
    return commandLine.getArguments();
  }

  /**
   * Gets the current instance for the global flags data store.  This method cannot be called until
   * {@link #initializeWith(CommandLine)} is successfully called.
   */
  public static Flags getInstance() {
    return Preconditions.checkNotNull(instance);
  }

  /**
   * Initialize the global flags data store with the given input commandline.  This method may only
   * be called once during an application's lifetime.
   */
  public static void initializeWith(CommandLine commandLine) {
    Preconditions.checkState(instance == null);
    overrideWith(commandLine);
  }

  @VisibleForTesting public static void overrideWith(CommandLine commandLine) {
    instance = new Flags(commandLine);
  }
}
