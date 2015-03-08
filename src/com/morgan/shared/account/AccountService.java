package com.morgan.shared.account;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.morgan.shared.auth.ClientUserInformation;
import com.morgan.shared.common.BackendException;
import com.morgan.shared.common.Role;

/**
 * Service interface for the account service used by the game.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RemoteServiceRelativePath("../services/account-service")
public interface AccountService extends RemoteService {

  /**
   * Creates a new account in the system with the given seed information.  The server will
   * create a temporary password for the user and will send them an email.
   */
  ClientUserInformation createAccount(String emailAddress, String displayName, Role memberRole)
      throws BackendException;
}
