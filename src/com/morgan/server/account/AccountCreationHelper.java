package com.morgan.server.account;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.morgan.server.auth.UserInformation;
import com.morgan.server.backend.UserBackend;
import com.morgan.server.common.CommonBindingAnnotations.Background;
import com.morgan.server.email.EmailValidator;
import com.morgan.server.game.GameServerFlagAccessor;
import com.morgan.server.util.log.AdvancedLogger;
import com.morgan.server.util.log.InjectLogger;
import com.morgan.shared.auth.AuthApplicationPlace;
import com.morgan.shared.common.BackendException;
import com.morgan.shared.common.Role;
import com.morgan.shared.nav.UrlCreator;

/**
 * Helper class for creating new accounts.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class AccountCreationHelper {

  @InjectLogger private AdvancedLogger log = AdvancedLogger.NULL;

  private final ListeningExecutorService backgroundService;
  private final AccountMessages messages;
  private final GameServerFlagAccessor flags;
  private final UserBackend userBackend;
  private final PasswordCreator passwordCreator;
  private final AccountSoyTemplate soy;
  private final Provider<HtmlEmail> htmlEmailProvider;
  private final UrlCreator urlCreator;

  @Inject AccountCreationHelper(
      @Background ListeningExecutorService backgroundService,
      AccountMessages messages,
      GameServerFlagAccessor flags,
      UserBackend userBackend,
      PasswordCreator passwordCreator,
      AccountSoyTemplate soy,
      Provider<HtmlEmail> htmlEmailProvider,
      UrlCreator urlCreator) {
    this.backgroundService = backgroundService;
    this.messages = messages;
    this.flags = flags;
    this.userBackend = userBackend;
    this.passwordCreator = passwordCreator;
    this.soy = soy;
    this.htmlEmailProvider = htmlEmailProvider;
    this.urlCreator = urlCreator;
  }

  private void sendNewAccountEmail(String emailAddress, String displayName, String password) {
    String serverTitle = flags.serverTitle();
    SafeUri serverUrl = urlCreator.createUrlFor(new AuthApplicationPlace());

    HtmlEmail email = htmlEmailProvider.get();
    try {
      email.setHtmlMsg(soy.createdEmailHtml(
          serverTitle,
          serverUrl,
          emailAddress,
          displayName,
          password));
      email.setTextMsg(soy.createdEmailPlain(
          serverTitle,
          serverUrl,
          emailAddress,
          displayName,
          password));
      email.setSubject(messages.newAccountSubject(emailAddress, serverTitle));
      email.addTo(emailAddress);
      backgroundService.execute(new EmailSender(emailAddress, email));
    } catch (EmailException e) {
      log.warning(e, "Error while trying to send account creation email to %s", emailAddress);
    }
  }

  UserInformation createAccount(String emailAddress, String displayName, Role memberRole)
      throws BackendException {
    EmailValidator.VALIDATOR.validate(emailAddress);
    Preconditions.checkArgument(!Strings.isNullOrEmpty(displayName));
    Preconditions.checkNotNull(memberRole);

    String password = passwordCreator.get();

    UserInformation result = userBackend.createAccount(
        emailAddress, displayName, password, memberRole);

    log.info("New account for %s <%s> created!", displayName, emailAddress);

    sendNewAccountEmail(emailAddress, displayName, password);

    return result;
  }

  /**
   * A task to actually send out an email.
   *
   * @author mark@mark-morgan.net (Mark Morgan)
   */
  private class EmailSender implements Runnable {

    private final String emailAddress;
    private final Email emailToSend;

    EmailSender(String emailAddress, Email emailToSend) {
      this.emailAddress = emailAddress;
      this.emailToSend = emailToSend;
    }

    @Override public void run() {
      try {
        emailToSend.send();
      } catch (EmailException e) {
        log.warning(e, "Error sending email to %s", emailAddress);
      }
    }
  }
}
