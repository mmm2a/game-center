package com.morgan.server.util.cmdline;

import com.google.common.base.Preconditions;

/**
 * Parser for parsing command lines from a standard command line (array of {@link String} instances.
 *
 * The format for arguments is as follows:
 *   <ul>
 *     <li>If an argument starts with a double-dash, its considered a long argument (everything
 *         up to the first = sign is the name, everything after is the value (optional)
 *     <li>If an argument starts with a single-dash, its a string of short flags (single letter
 *         flag names).  The last one is allowed to have an = sign and an arbitrarily long
 *         value string
 *     <li>Flags with no value are considered to have a value of true (unless they start with
 *         the letters "no", in which case, the no is removed and the value is false.  Therefor, the
 *         flag --noverbose is the same as --verbose=false
 *     <li>Arguments with no initial dashes are considered raw arguments.
 *     <li>An argument with only the value "--" is ommitted, and everything following is assumed
 *         to be non-flag arguments
 *   </ul>
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class CommandLineParser implements CommandLineSupplier {

  private final String[] rawCommandLine;

  public CommandLineParser(String[] rawCommandLine) {
    this.rawCommandLine = Preconditions.checkNotNull(rawCommandLine);
  }

  private void addBooleanFlagTo(CommandLine.Builder builder, String rawFlag) {
    boolean value = true;

    if (rawFlag.startsWith("no")) {
      rawFlag = rawFlag.substring(2);
      value = false;
    }

    builder.addBooleanFlag(rawFlag, value);
  }

  private void addLongFlagTo(CommandLine.Builder builder, String rawFlag) {
    int index = rawFlag.indexOf('=');
    Preconditions.checkState(index != 0, "Invalid flag parsed on command line: " + rawFlag);

    if (index < 0) {
      // No equals
      addBooleanFlagTo(builder, rawFlag);
    } else {
      // Has equals
      String name = rawFlag.substring(0, index);
      String value = rawFlag.substring(index + 1);
      Preconditions.checkState(!value.isEmpty(), "Invalid flag parsed on command line: " + rawFlag);
      builder.addFlag(name, value);
    }
  }

  private void addShortFlagsTo(CommandLine.Builder builder, String rawFlags) {
    int index = rawFlags.indexOf('=');
    Preconditions.checkState(index != 0, "Invalid flag parsed on command line: " + rawFlags);

    if (index > 0) {
      addLongFlagTo(builder, rawFlags.substring(index - 1));
    }

    if (index < 0) {
      index = rawFlags.length() + 1;
    }

    for (int i = 0; i < index - 1; i++) {
      builder.addBooleanFlag(Character.toString(rawFlags.charAt(i)), true);
    }
  }

  @Override public CommandLine.Builder supplyCommandLineContents(CommandLine.Builder builder) {
    boolean parseFlags = true;

    for (String rawArg : rawCommandLine) {
      if (rawArg.isEmpty()) {
        continue;
      }

      if (parseFlags && rawArg.startsWith("-")) {
        if (rawArg.equals("--")) {
          parseFlags = false;
          continue;
        }

        if (rawArg.startsWith("--")) {
          addLongFlagTo(builder, rawArg.substring(2));
        } else {
          addShortFlagsTo(builder, rawArg.substring(1));
        }
      } else {
        builder.addArgument(rawArg);
      }
    }

    return builder;
  }
}
