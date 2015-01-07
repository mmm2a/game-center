package com.morgan.server.backend.prod;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests for the {@link ProdAlarmBackend} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class ProdAlarmBackendTest {

  @Test public void justFail() {
    assertThat(false).isTrue();
  }
}
