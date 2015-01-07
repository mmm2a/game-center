package com.morgan.server.backend.prod.alarmdb;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests for the {@link AlarmDbHelper} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class AlarmDbHelperTest {

  @Test public void justFail() {
    assertThat(false).isTrue();
  }
}
