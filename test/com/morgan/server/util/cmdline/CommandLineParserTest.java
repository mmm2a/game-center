package com.morgan.server.util.cmdline;

import org.junit.Test;

import com.google.common.truth.Truth;
import com.morgan.server.util.cmdline.CommandLine;
import com.morgan.server.util.cmdline.CommandLineParser;

/**
 * Tests for the {@link CommandLineParser} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class CommandLineParserTest {

  private void doTestSupplyCommandLineContents(
      CommandLine.Builder exemplarBuilder, String...arguments) {
    CommandLine.Builder testBuilder = CommandLine.builder();
    new CommandLineParser(arguments).supplyCommandLineContents(testBuilder);
    Truth.assertThat(testBuilder.build()).isEqualTo(exemplarBuilder.build());
  }

  @Test public void supplyCommandLineContents_longFlagWithValue() {
    doTestSupplyCommandLineContents(
        CommandLine.builder().addFlag("flag", "value"),
        "--flag=value");
  }

  @Test public void supplyCommandLineContents_longFlagWithoutValue() {
    doTestSupplyCommandLineContents(
        CommandLine.builder().addFlag("flag", "true"),
        "--flag");
  }

  @Test public void supplyCommandLineContents_longFlagWithoutValue_isNegated() {
    doTestSupplyCommandLineContents(
        CommandLine.builder().addFlag("flag", "false"),
        "--noflag");
  }

  @Test public void supplyCommandLineContents_shortFlagsWithValue() {
    doTestSupplyCommandLineContents(
        CommandLine.builder().addFlag("a", "true").addFlag("b", "true").addFlag("c", "value"),
        "-abc=value");
  }

  @Test public void supplyCommandLineContents_shortFlagsWithoutValue() {
    doTestSupplyCommandLineContents(
        CommandLine.builder().addFlag("a", "true").addFlag("b", "true").addFlag("c", "true"),
        "-abc");
  }

  @Test public void supplyCommandLineContents_arguments() {
    doTestSupplyCommandLineContents(
        CommandLine.builder().addArgument("long-argument"),
        "long-argument");
  }

  @Test public void supplyCommandLineContents_arguments_afterBarrier() {
    doTestSupplyCommandLineContents(
        CommandLine.builder().addArgument("--long-argument"),
        "--", "--long-argument");
  }
}
