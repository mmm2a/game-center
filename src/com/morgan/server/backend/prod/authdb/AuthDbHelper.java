package com.morgan.server.backend.prod.authdb;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.morgan.server.auth.UserInformation;

/**
 * A helper class for helping with database access related to authentication.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AuthDbHelper {

  private final EntityManager entityManager;

  @Inject AuthDbHelper(EntityManager entityManager) {
    this.entityManager = entityManager;
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
}
