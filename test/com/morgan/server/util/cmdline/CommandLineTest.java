package com.morgan.server.util.cmdline;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.truth.Truth;

/**
 * Tests for the {@link CommandLine} class (more specifically, the builder in it).
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class CommandLineTest {

  private static final String FILENAME = "the-filename";

  private TestableBuilder builder;

  @Before public void createTestInstances() {
    builder = new TestableBuilder();
  }

  @Test public void build_usesSubBuilder() {
    builder.addFlag("parent-flag", "parent-value");
    builder.addFlag(CommandLine.CONFIGURATION_FLAG, FILENAME);
    Truth.assertThat(builder.build()).isEqualTo(CommandLine.builder()
        .addFlag("parent-flag", "parent-value")
        .addFlag("sub-flag", "sub-value")
        .build());
  }

  @Test public void build_commandLineGivesFlagFirst_subBuilderOverridden() {
    builder.addFlag("parent-flag", "parent-value");
    builder.addFlag("sub-flag", "parent-value-2");
    builder.addFlag(CommandLine.CONFIGURATION_FLAG, FILENAME);
    Truth.assertThat(builder.build()).isEqualTo(CommandLine.builder()
        .addFlag("parent-flag", "parent-value")
        .addFlag("sub-flag", "parent-value-2")
        .build());
  }

  @Test public void build_commandLineGivesFlagLast_subBuilderOverridden() {
    builder.addFlag("parent-flag", "parent-value");
    builder.addFlag(CommandLine.CONFIGURATION_FLAG, FILENAME);
    builder.addFlag("sub-flag", "parent-value-2");
    Truth.assertThat(builder.build()).isEqualTo(CommandLine.builder()
        .addFlag("parent-flag", "parent-value")
        .addFlag("sub-flag", "parent-value-2")
        .build());
  }

  private static class TestableBuilder extends CommandLine.Builder {
    @Override void addChildConfiguration(String filename, CommandLine.Builder subBuilder) {
      Preconditions.checkArgument(filename.equals(FILENAME));
      subBuilder.addFlag("sub-flag", "sub-value");
    }
  }
}
