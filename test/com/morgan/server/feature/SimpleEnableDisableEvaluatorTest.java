package com.morgan.server.feature;

import org.junit.Before;
import org.junit.Test;

import com.google.common.truth.Truth;

/**
 * Tests for the {@link SimpleEnableDisableEvaluator} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class SimpleEnableDisableEvaluatorTest {

  private SimpleEnableDisableEvaluator evaluator;

  @Before public void createTestInstances() {
    evaluator = new SimpleEnableDisableEvaluator();
  }

  @Test public void shouldEnable_enabled() {
    Truth.assertThat(evaluator.shouldEnable("enabled").get()).isTrue();
  }

  @Test public void shouldEnable_disabled() {
    Truth.assertThat(evaluator.shouldEnable("disabled").get()).isFalse();
  }

  @Test public void shouldEnable_unknown() {
    Truth.assertThat(evaluator.shouldEnable("unknown").isPresent()).isFalse();
  }
}
