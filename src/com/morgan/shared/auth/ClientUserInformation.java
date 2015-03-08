package com.morgan.shared.auth;

import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.morgan.shared.common.Role;

/**
 * Data class for representing on the client side a user's information.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class ClientUserInformation implements IsSerializable {

  private String displayName;
  private Optional<Role> memberRole;

  private ClientUserInformation() {
    // Default constructor for GWT
  }

  private ClientUserInformation(String displayName, Optional<Role> memberRole) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(displayName));
    this.displayName = displayName;
    this.memberRole = memberRole;
  }

  public String getDisplayName() {
    return displayName;
  }

  public Optional<Role> getMemberRole() {
    return memberRole;
  }

  @Override public int hashCode() {
    return Objects.hash(displayName, memberRole);
  }

  @Override public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof ClientUserInformation)) {
      return false;
    }

    ClientUserInformation other = (ClientUserInformation) o;
    return displayName.equals(other.displayName)
        && memberRole.equals(other.memberRole);
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(ClientUserInformation.class)
        .add("displayName", displayName)
        .add("memberRole", memberRole)
        .toString();
  }

  /**
   * Creates a new {@link ClientUserInformation} instance but withholds adding the member role which
   * is considered privlidged.
   */
  public static ClientUserInformation withHiddenRole(String displayName) {
    return new ClientUserInformation(displayName, Optional.<Role>absent());
  }

  /**
   * Creates a new {@link ClientUserInformation} instance with privlidged information included.
   */
  public static ClientUserInformation withPrivlidgedInformation(
      String displayName, Role memberRole) {
    return new ClientUserInformation(displayName, Optional.of(memberRole));
  }
}
