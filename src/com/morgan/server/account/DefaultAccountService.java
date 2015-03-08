package com.morgan.server.account;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morgan.server.util.log.AdvancedLogger;
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

  private static final AdvancedLogger log = new AdvancedLogger(DefaultAccountService.class);

  @Inject DefaultAccountService() {
  }

  @Override public ClientUserInformation createAccount(
      String emailAddress,
      String displayName,
      Role memberRole) throws BackendException {
    log.info("New account for %s <%s> created!", displayName, emailAddress);
    return ClientUserInformation.withPrivlidgedInformation(displayName, memberRole);
  }
}
