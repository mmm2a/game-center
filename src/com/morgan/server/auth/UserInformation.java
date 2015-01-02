package com.morgan.server.auth;

import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.morgan.shared.common.Role;

/**
 * Data class describing a user.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class UserInformation {

  private final long userId;
  private final String displayName;
  private final String emailAddress;
  private final Role userRole;

  public UserInformation(long userId, String displayName, String emailAddress, Role userRole) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(displayName));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(emailAddress));
    this.userId = userId;
    this.displayName = displayName;
    this.emailAddress = emailAddress;
    this.userRole = Preconditions.checkNotNull(userRole);
  }

  public long getUserId() {
    return userId;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public Role getUserRole() {
    return userRole;
  }

  @Override public int hashCode() {
    return Objects.hash(userId, displayName, emailAddress, userRole);
  }

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof UserInformation)) {
      return false;
    }

    UserInformation other = (UserInformation) o;
    return userId == other.userId
        && displayName.equals(other.displayName)
        && emailAddress.equals(other.emailAddress)
        && userRole == other.userRole;
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(UserInformation.class)
        .add("userId", userId)
        .add("displayName", displayName)
        .add("emailAddress", emailAddress)
        .add("userRole", userRole)
        .toString();
  }
}
