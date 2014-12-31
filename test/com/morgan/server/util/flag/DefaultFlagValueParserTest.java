package com.morgan.server.util.flag;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.google.common.truth.Truth;

/**
 * Tests for the {@link DefaultFlagValueParser} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class DefaultFlagValueParserTest {

  private DefaultFlagValueParser parser;

  @Before public void createTestInstances() {
    parser = new DefaultFlagValueParser();
  }

  @Test public void parseStringRepresentation_stringValue() {
    Truth.assertThat(parser.parseStringRepresentation(String.class, "foo")).isEqualTo("foo");
  }

  @Test public void parseStringRepresentation_enumValue() {
    Truth.assertThat(parser.parseStringRepresentation(TimeUnit.class, "SECONDS"))
        .isEqualTo(TimeUnit.SECONDS);
  }

  @Test public void parseStringRepresentation_booleanValue() {
    Truth.assertThat(parser.parseStringRepresentation(boolean.class, "true")).isEqualTo(true);
    Truth.assertThat(parser.parseStringRepresentation(Boolean.class, "true")).isEqualTo(true);
  }

  @Test public void parseStringRepresentation_integerValue() {
    Truth.assertThat(parser.parseStringRepresentation(int.class, "7")).isEqualTo(7);
    Truth.assertThat(parser.parseStringRepresentation(Integer.class, "7")).isEqualTo(7);
  }

  @Test(expected = IllegalStateException.class)
  public void parseStringRepresentation_unexpectedType() {
    parser.parseStringRepresentation(Thread.class, "anything");
  }
}
