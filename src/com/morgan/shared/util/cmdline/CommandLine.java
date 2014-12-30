package com.morgan.shared.util.cmdline;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * A container class representing the flags and arguments passed on the command line to a binary
 * program.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class CommandLine {

  private final ImmutableMap<String, String> flagStringValuesMap;
  private final ImmutableList<String> additionalArgs;

  private CommandLine(Map<String, String> flagStringValuesMap, Iterable<String> additionalArgs) {
    this.flagStringValuesMap = ImmutableMap.copyOf(flagStringValuesMap);
    this.additionalArgs = ImmutableList.copyOf(additionalArgs);
  }

  public ImmutableMap<String, String> getFlagMap() {
    return flagStringValuesMap;
  }

  public ImmutableList<String> getArguments() {
    return additionalArgs;
  }

  @Override public int hashCode() {
    return Objects.hash(flagStringValuesMap, additionalArgs);
  }

  @Override public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof CommandLine)) {
      return false;
    }

    CommandLine other = (CommandLine) o;
    return flagStringValuesMap.equals(other.flagStringValuesMap)
        && additionalArgs.equals(other.additionalArgs);
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(CommandLine.class)
        .add("flagStringValuesMap", flagStringValuesMap)
        .add("additionalArgs", additionalArgs)
        .toString();
  }

  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder class for the {@link CommandLine} class instances.
   *
   * @author mark@mark-morgan.net (Mark Morgan)
   */
  public static class Builder {

    private final Map<String, String> flags = new HashMap<>();
    private final ImmutableList.Builder<String> argumentsBuilder = ImmutableList.builder();

    private Builder() {
      // Do not instantiate directly
    }

    public Builder addBooleanFlag(String flagName, boolean isOn) {
      return addFlag(flagName, isOn ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
    }

    public Builder addFlag(String flagName, String flagValue) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(flagName));
      Preconditions.checkArgument(!Strings.isNullOrEmpty(flagValue));
      flags.put(flagName, flagValue);
      return this;
    }

    public Builder addArgument(String argument) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(argument));
      argumentsBuilder.add(argument);
      return this;
    }

    public CommandLine build() {
      return new CommandLine(flags, argumentsBuilder.build());
    }
  }
}
