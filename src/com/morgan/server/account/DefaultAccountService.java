package com.morgan.server.account;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morgan.server.auth.AuthorizedFor;
import com.morgan.server.auth.UserInformation;
import com.morgan.server.backend.UserBackend;
import com.morgan.server.email.EmailValidator;
import com.morgan.server.util.log.AdvancedLogger;
import com.morgan.server.util.log.InjectLogger;
import com.morgan.shared.account.AccountService;
import com.morgan.shared.auth.ClientUserInformation;
import com.morgan.shared.common.BackendException;
import com.morgan.shared.common.Role;

/**
 * Default implementation of the {@link AccountService} service interface.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
class DefaultAccountService extends RemoteServiceServlet implements AccountService {

  static final long serialVersionUID = 1L;

  private final UserBackend userBackend;
  private final PasswordCreator passwordCreator;
  private final Function<UserInformation, ClientUserInformation> userInformationConverter;

  @InjectLogger private AdvancedLogger log = AdvancedLogger.NULL;

  @Inject DefaultAccountService(
      UserBackend userBackend,
      PasswordCreator passwordCreator,
      Function<UserInformation, ClientUserInformation> userInformationConverter) {
    this.userBackend = userBackend;
    this.passwordCreator = passwordCreator;
    this.userInformationConverter = userInformationConverter;
  }

  @Override
  @AuthorizedFor(Role.ADMIN)
  public ClientUserInformation createAccount(
      String emailAddress,
      String displayName,
      Role memberRole) throws BackendException {
    EmailValidator.VALIDATOR.validate(emailAddress);
    Preconditions.checkArgument(!Strings.isNullOrEmpty(displayName));
    Preconditions.checkNotNull(memberRole);

    String password = passwordCreator.get();

    ClientUserInformation result = userInformationConverter.apply(
        userBackend.createAccount(emailAddress, displayName, password, memberRole));

    log.info("New account for %s <%s> created!", displayName, emailAddress);
    return result;
  }
}
