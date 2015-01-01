package com.morgan.server.util.flag;

import java.lang.reflect.Type;

/**
 * An interface for a type that can parse a flag's value by parsing its string representation.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface FlagValueParser {

  /**
   * Parse the given flag's string representation into a value type.
   */
  Object parseStringRepresentation(Type valueType, String representation);
}
