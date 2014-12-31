package com.morgan.shared.macro;

import java.util.Properties;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.truth.Truth;
import com.morgan.shared.util.macro.Macros;

/**
 * Tests for the {@link Macros} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class MacrosTest {

    private static final Function<String, String> variableFunction = new Function<String, String>() {
      @Override public String apply(String input) {
        return input.toUpperCase();
      }
    };


  @Test public void macroReplacementFunction_functionVariableSatisfier() {
    Function<String, String> macroFunction = Macros.macroReplacementFunction(variableFunction);
    Truth.assertThat(macroFunction.apply("alpha ${beta} gamma")).isEqualTo("alpha BETA gamma");
  }

  @Test public void macroReplacementFunction_mapVariableSatisfier() {
    Function<String, String> macroFunction = Macros.macroReplacementFunction(
        ImmutableMap.of("beta", "BETA"));
    Truth.assertThat(macroFunction.apply("alpha ${beta} gamma")).isEqualTo("alpha BETA gamma");
  }

  @Test public void macroReplacementFunction_propertiesVariableSatisfier() {
    Properties props = new Properties();
    props.setProperty("beta", "BETA");
    Function<String, String> macroFunction = Macros.macroReplacementFunction(props);
    Truth.assertThat(macroFunction.apply("alpha ${beta} gamma")).isEqualTo("alpha BETA gamma");
  }

  @Test public void macroReplacementFunction_multipleVariables() {
    Function<String, String> macroFunction = Macros.macroReplacementFunction(variableFunction);
    Truth.assertThat(macroFunction.apply("${alpha} ${beta} ${gamma}"))
        .isEqualTo("ALPHA BETA GAMMA");
  }
}
