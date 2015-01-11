package com.morgan.server.util.macro;

import java.util.Map;
import java.util.Properties;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Preconditions;

/**
 * Factory class for replacing macros in strings.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class Macros {

  private Macros() {
    // Do not instantiate
  }

  /**
   * Returns a {@link Function} that replaces macros in a string with values identified from the
   * replacement function.  Variables which are undefined get replaces as empty strings.
   *
   * <p>Macros have the format <code><b>${</b> <i>var-name</i> <b>}</b></code>
   */
  public static Function<String, String> macroReplacementFunction(
      final Function<String, String> variableReplacementFunction) {
    return new MacroReplacingFunction(variableReplacementFunction);
  }

  /**
   * As {@link #macroReplacementFunction(Function)}, but variable definitions come from the map.
   */
  public static Function<String, String> macroReplacementFunction(
      Map<String, String> variableDefinitions) {
    return macroReplacementFunction(Functions.forMap(variableDefinitions));
  }

  /**
   * As {@link #macroReplacementFunction(Function)}, but variable definitions come from the
   * properties object.
   */
  public static Function<String, String> macroReplacementFunction(Properties variableDefinitions) {
    return macroReplacementFunction(new PropertiesFunction(variableDefinitions));
  }

  /**
   * Internal implementation of a {@link Function} that replaces macros in a string with their
   * values.
   */
  private static final class MacroReplacingFunction implements Function<String, String> {

    private final Function<String, String> variableReplacementFunction;

    private MacroReplacingFunction(Function<String, String> variableReplacementFunction) {
      this.variableReplacementFunction = Preconditions.checkNotNull(variableReplacementFunction);
    }

    @Override @Nullable public String apply(@Nullable String input) {
      if (input == null) {
        return null;
      }

      StringBuilder builder = new StringBuilder();

      int index;
      int end = 0;
      while ( (index = input.indexOf("${", end)) >= 0) {
        builder.append(input.substring(end, index));
        end = input.indexOf("}", index);
        if (end < index) {
          end = index;
          break;
        }

        String variableName = input.substring(index + 2, end);
        String variableValue = variableReplacementFunction.apply(variableName);
        if (variableValue == null) {
          variableValue = "";
        }
        builder.append(variableValue);
        end++;
      }

      builder.append(input.substring(end));
      return builder.toString();
    }
  }

  /**
   * Internal function that turns a {@link Properties} object into a function that returns the
   * property value for an input key.
   */
  private static final class PropertiesFunction implements Function<String, String> {

    private final Properties properties;

    private PropertiesFunction(Properties properties) {
      this.properties = Preconditions.checkNotNull(properties);
    }

    @Override @Nullable public String apply(@Nullable String input) {
      return input == null ? null : properties.getProperty(input);
    }
  }
}
