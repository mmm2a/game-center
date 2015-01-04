package com.morgan.server.backend.prod.authdb;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests for the {@link AuthDbHelper} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthDbHelperTest {

  @Test public void justFail() {
    assertThat(false).isTrue();
  }
}
