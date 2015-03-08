package com.morgan.server.backend.prod.authdb;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.morgan.server.auth.UserInformation;
import com.morgan.shared.common.BackendException;
import com.morgan.shared.common.Role;

/**
 * A helper class for helping with database access related to authentication.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AuthDbHelper {

  private final Provider<EntityManager> entityManagerProvider;

  @Inject AuthDbHelper(Provider<EntityManager> entityManagerProvider) {
    this.entityManagerProvider = entityManagerProvider;
  }

  @Nullable private UserInformation convertUserInformationEntityToUserInformation(
      @Nullable UserInformationEntity entity) {
    if (entity == null) {
      return null;
    }

    return new UserInformation(
        entity.getId(),
        entity.getDisplayName(),
        entity.getEmailAddress(),
        entity.getRole());
  }

  /**
   * Find the user indicated by the user id.  If the user can't be found, then return
   * {@link Optional#absent()}.
   */
  @Transactional
  public Optional<UserInformation> findUserById(long id) {
    EntityManager entityManager = entityManagerProvider.get();
    return Optional.fromNullable(convertUserInformationEntityToUserInformation(
        entityManager.find(UserInformationEntity.class, id)));
  }

  /**
   * Attempt to log in as the indicated user.  If the user is found, and the authentication is
   * successful, then return the {@link UserInformation} associated with that user.  Otherwise,
   * returns {@link Optional#absent()}.
   */
  @Transactional
  public Optional<UserInformation> authenticate(String emailAddress, String password) {
    EntityManager entityManager = entityManagerProvider.get();
    UserInformationEntity entity = Iterables.getOnlyElement(
        entityManager.createNamedQuery("findUserByEmail", UserInformationEntity.class)
            .setParameter("emailAddress", emailAddress.toLowerCase())
            .getResultList(),
        null);
    if (entity != null && entity.getAuthenticationEntity().getPassword().equals(password)) {
      return Optional.of(convertUserInformationEntityToUserInformation(entity));
    }

    return Optional.absent();
  }

  /**
   * Attempt to create a new user account.
   */
  @Transactional
  public UserInformation createAccount(
      String emailAddress,
      String displayName,
      String password,
      Role memberRole) throws BackendException {
    EntityManager entityManager = entityManagerProvider.get();
    AuthenticationEntity authEntity = new AuthenticationEntity(password);
    UserInformationEntity userEntity = new UserInformationEntity(
        emailAddress, displayName, memberRole);
    userEntity.setAuthenticationEntity(authEntity);
    try {
      entityManager.persist(userEntity);
      return new UserInformation(userEntity.getId(), displayName, emailAddress, memberRole);
    } catch (Throwable cause) {
      throw new BackendException("Unable to create user account", cause);
    }
  }
}
