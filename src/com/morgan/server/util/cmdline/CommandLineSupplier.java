package com.morgan.server.util.cmdline;

/**
 * Interface for a type that supplies flags and arguments to a {@link CommandLine.Builder}
 * instance.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface CommandLineSupplier {

  /**
   * Supplies command line arguments and flags to an existing builder.
   */
  CommandLine.Builder supplyCommandLineContents(CommandLine.Builder builder);
}
