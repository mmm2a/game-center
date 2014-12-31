package com.morgan.server.util.cmdline;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.truth.Truth;

/**
 * Tests for the {@link ArgumentConfigurationParser} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class ArgumentConfigurationParserTest {

  private static final String PROPERTIES_FILE_CONTENTS =
      "flag-1=value-1\n"
      + "flag-2=value ${alpha} 2\n"
      + "flag-3=value-3";
  private static final String FILENAME = "the-filename";
  private static final ImmutableMap<String, String> ENV =
      ImmutableMap.of("alpha", "ALPHA");

  private TestableArgumentConfigurationParser parser;

  @Before public void createTestInstances() {
    parser = new TestableArgumentConfigurationParser();
  }

  @Test public void supplyCommandLineContents() {
    Truth.assertThat(parser.supplyCommandLineContents(CommandLine.builder()).build())
        .isEqualTo(CommandLine.builder()
            .addFlag("flag-1", "value-1")
            .addFlag("flag-2", "value ALPHA 2")
            .addFlag("flag-3", "value-3")
            .build());
  }

  private static final class TestableArgumentConfigurationParser
      extends ArgumentConfigurationParser {

    private TestableArgumentConfigurationParser() {
      super(FILENAME);
    }

    @Override Reader openFile(String filename) throws IOException {
      Preconditions.checkArgument(filename.equals(FILENAME));
      return new StringReader(PROPERTIES_FILE_CONTENTS);
    }

    @Override Map<String, String> getEnvironment() {
      return ENV;
    }
  }
}
