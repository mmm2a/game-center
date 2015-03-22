package com.morgan.server.account;

import com.google.common.base.Function;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morgan.server.auth.AuthorizedFor;
import com.morgan.server.auth.UserInformation;
import com.morgan.server.util.stat.MeasureStatistics;
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

  private final AccountCreationHelper creationHelper;
  private final Function<UserInformation, ClientUserInformation> userInformationConverter;

  @Inject DefaultAccountService(
      AccountCreationHelper creationHelper,
      Function<UserInformation, ClientUserInformation> userInformationConverter) {
    this.creationHelper = creationHelper;
    this.userInformationConverter = userInformationConverter;
  }

  @Override
  @AuthorizedFor(Role.ADMIN)
  @MeasureStatistics
  public ClientUserInformation createAccount(
      String emailAddress,
      String displayName,
      Role memberRole) throws BackendException {
    return userInformationConverter.apply(
        creationHelper.createAccount(emailAddress, displayName, memberRole));
  }
}
