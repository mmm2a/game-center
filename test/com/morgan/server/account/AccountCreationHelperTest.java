package com.morgan.server.account;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.mail.HtmlEmail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.inject.util.Providers;
import com.morgan.server.auth.UserInformation;
import com.morgan.server.backend.UserBackend;
import com.morgan.server.game.GameServerFlagAccessor;
import com.morgan.server.util.soy.fake.FakeSoyTemplateFactory;
import com.morgan.shared.auth.AuthApplicationPlace;
import com.morgan.shared.common.Role;
import com.morgan.shared.nav.UrlCreator;

/**
 * Tests for the {@link AccountCreationHelper} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountCreationHelperTest {

  private static final String EMAIL = "foo@bar.com";
  private static final String DISPLAY = "Foo Bar";
  private static final Role ROLE = Role.MEMBER;
  private static final String PASSWORD = "password";
  private static final String SERVER_TITLE = "server title";

  private static final UserInformation USER_INFO =
      new UserInformation(7L, DISPLAY, EMAIL, ROLE);

  private static final SafeUri SERVER_URL = UriUtils.fromString("http://tempuri.org");

  private static final ListeningExecutorService BACKGROUND_SERVICE =
      MoreExecutors.newDirectExecutorService();
  private static final AccountMessages MESSAGES = new AccountMessages();

  private static final AccountSoyTemplate SOY =
      FakeSoyTemplateFactory.createProxyFor(AccountSoyTemplate.class);

  @Mock private GameServerFlagAccessor mockFlags;
  @Mock private UserBackend mockBackend;
  @Mock private PasswordCreator mockPasswordCreator;
  @Mock private HtmlEmail mockEmail;
  @Mock private UrlCreator mockUrlCreator;

  private AccountCreationHelper helper;

  @Before public void createTestInstances() {
    helper = new AccountCreationHelper(
        BACKGROUND_SERVICE,
        MESSAGES,
        mockFlags,
        mockBackend,
        mockPasswordCreator,
        SOY,
        Providers.of(mockEmail),
        mockUrlCreator);
  }

  @Before public void setUpCommonMockInteractions() throws Exception {
    when(mockPasswordCreator.get()).thenReturn(PASSWORD);
    when(mockBackend.createAccount(EMAIL, DISPLAY, PASSWORD, ROLE)).thenReturn(USER_INFO);
    when(mockFlags.serverTitle()).thenReturn(SERVER_TITLE);
    when(mockUrlCreator.createUrlFor(new AuthApplicationPlace())).thenReturn(SERVER_URL);
  }

  @Test public void createAccount_createsAccountInBackend() throws Exception {
    assertThat(helper.createAccount(EMAIL, DISPLAY, ROLE)).isEqualTo(USER_INFO);
  }

  @Test public void createAccount_sendsEmail() throws Exception {
    helper.createAccount(EMAIL, DISPLAY, ROLE);
    verify(mockEmail)
        .setHtmlMsg(SOY.createdEmailHtml(SERVER_TITLE, SERVER_URL, EMAIL, DISPLAY, PASSWORD));
    verify(mockEmail)
        .setTextMsg(SOY.createdEmailPlain(SERVER_TITLE, SERVER_URL, EMAIL, DISPLAY, PASSWORD));
    verify(mockEmail).setSubject(MESSAGES.newAccountSubject(EMAIL, SERVER_TITLE));
    verify(mockEmail).addTo(EMAIL);
    verify(mockEmail).send();
  }
}
