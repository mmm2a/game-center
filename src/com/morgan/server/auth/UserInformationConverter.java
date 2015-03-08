package com.morgan.server.auth;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.morgan.server.common.CommonBindingAnnotations.RequestUser;
import com.morgan.shared.auth.ClientUserInformation;
import com.morgan.shared.common.Role;

/**
 * Function class for converting from {@link UserInformation} instances to
 * {@link ClientUserInformation} instances.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class UserInformationConverter implements Function<UserInformation, ClientUserInformation>{

  private final Provider<Optional<UserInformation>> requestUserProvider;

  @Inject UserInformationConverter(
      @RequestUser Provider<Optional<UserInformation>> requestUserProvider) {
    this.requestUserProvider = requestUserProvider;
  }

  @Override @Nullable public ClientUserInformation apply(@Nullable UserInformation input) {
    if (input == null) {
      return null;
    }

    UserInformation requester = requestUserProvider.get().get();
    if (requester.getUserRole() == Role.ADMIN) {
      return ClientUserInformation.withPrivlidgedInformation(
          input.getDisplayName(), input.getUserRole());
    } else {
      return ClientUserInformation.withHiddenRole(input.getDisplayName());
    }
  }
}
