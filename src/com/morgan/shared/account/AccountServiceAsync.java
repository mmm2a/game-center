package com.morgan.shared.account;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.morgan.shared.auth.ClientUserInformation;
import com.morgan.shared.common.Role;

/**
 * Asynchronous version of the {@link AccountService}.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface AccountServiceAsync {

  /** See {@link AccountService#createAccount(String, String, Role)} */
  void createAccount(
      String emailAddress,
      String displayName,
      Role memberRole,
      AsyncCallback<ClientUserInformation> callback);
}
