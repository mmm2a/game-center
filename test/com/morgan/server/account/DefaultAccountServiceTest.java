package com.morgan.server.account;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.morgan.shared.auth.ClientUserInformation;
import com.morgan.shared.common.Role;

/**
 * Tests for the {@link DefaultAccountService} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultAccountServiceTest {

  private DefaultAccountService service;

  @Before public void createTestInstances() {
    service = new DefaultAccountService();
  }

  @Test public void createAccount_returnsCorrectInformation() throws Exception {
    assertThat(service.createAccount("email address", "display name", Role.MEMBER))
        .isEqualTo(ClientUserInformation.withPrivlidgedInformation("display name", Role.MEMBER));
  }
}
