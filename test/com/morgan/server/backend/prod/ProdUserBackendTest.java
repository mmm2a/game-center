package com.morgan.server.backend.prod;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests for the {@link ProdUserBackend} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class ProdUserBackendTest {

  @Test public void justFail() {
    assertThat(false).isTrue();
  }
}
