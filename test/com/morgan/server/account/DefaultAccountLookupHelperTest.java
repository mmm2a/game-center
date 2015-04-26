package com.morgan.server.account;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.morgan.server.auth.UserInformation;
import com.morgan.server.backend.UserBackend;
import com.morgan.shared.common.Role;

/**
 * Tests for the {@link DefaultAccountLookupHelper} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultAccountLookupHelperTest {

  @Mock private UserBackend mockBackend;

  private DefaultAccountLookupHelper helper;

  @Before public void createTestInstances() {
    helper = new DefaultAccountLookupHelper(mockBackend);
  }

  @Test public void lookUpAccounts_empty() throws Exception {
    assertThat(helper.lookUpAccounts(ImmutableSet.<Long>of())).isEmpty();
  }

  @Test public void lookUpAccounts() throws Exception {
    UserInformation info1 = new UserInformation(7L, "display 1", "email1", Role.MEMBER);
    UserInformation info2 = new UserInformation(42L, "display 2", "email2", Role.MEMBER);
    UserInformation info3 = new UserInformation(69L, "display 3", "email3", Role.MEMBER);

    ImmutableMap<Long, UserInformation> result = ImmutableMap.of(
        7L, info1,
        42L, info2,
        69L, info3);
    when(mockBackend.findUsersById(ImmutableSet.of(7L, 42L, 69L))).thenReturn(result);
    result = helper.lookUpAccounts(ImmutableSet.of(7L, 42L, 69L));
    assertThat(result).hasSize(3);
    assertThat(result).containsEntry(7L, info1);
    assertThat(result).containsEntry(42L, info2);
    assertThat(result).containsEntry(69L, info3);
  }
}
