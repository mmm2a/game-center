package com.morgan.shared.nav;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;


/**
 * Tests for the {@link ClientApplication} enum.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class ClientApplicationTest {

  @Test public void fromPathComponent() {
    for (ClientApplication application : ClientApplication.values()) {
      assertThat(ClientApplication.fromPathComponent(application.getApplicationPathToken()))
          .isEqualTo(application);
    }
  }
}
