package com.morgan.server.email;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests for the {@link AbstractEmailProvider} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractEmailProviderTest {

  private static final String HOST = "host";
  private static final int PORT = 69;
  private static final String USER = "user";
  private static final String PASSWORD = "password";
  private static final boolean IS_SSL = true;
  private static final String FROM = "from";

  @Mock private EmailFlagAccessor mockEmailFlagAccessor;
  @Mock private Email mockEmail;

  private TestableEmailProvider provider;

  @Before public void createTestInstances() {
    provider = new TestableEmailProvider();
  }

  @Before public void setUpCommonMockInteractions() {
    when(mockEmailFlagAccessor.smtpHostname()).thenReturn(HOST);
    when(mockEmailFlagAccessor.smtpPort()).thenReturn(PORT);
    when(mockEmailFlagAccessor.smtpUserName()).thenReturn(USER);
    when(mockEmailFlagAccessor.smtpPassword()).thenReturn(PASSWORD);
    when(mockEmailFlagAccessor.isSslOnConnect()).thenReturn(IS_SSL);
    when(mockEmailFlagAccessor.fromAddress()).thenReturn(FROM);
  }

  private void verifyCommonEmailSettings() throws Exception {
    verify(mockEmail).setHostName(HOST);
    verify(mockEmail).setSmtpPort(PORT);
    verify(mockEmail).setSSLOnConnect(IS_SSL);
    verify(mockEmail).setFrom(FROM);
  }

  @Test public void get_noUserName_doesNotSetAuth() throws Exception {
    when(mockEmailFlagAccessor.smtpUserName()).thenReturn(null);
    assertThat(provider.get()).is(mockEmail);
    verifyCommonEmailSettings();
    verifyNoMoreInteractions(mockEmail);
  }

  @Test public void get() throws Exception {
    assertThat(provider.get()).is(mockEmail);
    verifyCommonEmailSettings();

    // There isn't a particularly good way to test the contents of the authenticator, so we just
    // hope for the best
    verify(mockEmail).setAuthenticator(isA(DefaultAuthenticator.class));
  }

  private class TestableEmailProvider extends AbstractEmailProvider<Email> {

    TestableEmailProvider() {
      super(mockEmailFlagAccessor);
    }

    @Override protected Email createInstance() {
      return mockEmail;
    }
  }
}
