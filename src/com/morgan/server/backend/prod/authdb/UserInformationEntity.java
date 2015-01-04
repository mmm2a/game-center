package com.morgan.server.backend.prod.authdb;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.morgan.shared.common.Role;

/**
 * Entity type for representing generic information about a user.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@NamedQueries({
  @NamedQuery(
      name = "findUserByEmail",
      query = "SELECT u FROM userinfo AS u WHERE u.emailAddress = :emailAddress")
})
@Entity(name = "userinfo")
public class UserInformationEntity {

  @Id @GeneratedValue
  private long id;

  @Column(length = 256, nullable = false, unique = true)
  private String emailAddress;

  @Column(length = 128, nullable = false)
  private String name;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToOne(cascade = {CascadeType.ALL})
  @PrimaryKeyJoinColumn
  private AuthenticationEntity authenticationEntity;

  UserInformationEntity() {
  }

  UserInformationEntity(String emailAddress, String name, Role role) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(emailAddress));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(name));

    this.emailAddress = emailAddress;
    this.name = name;
    this.role = Preconditions.checkNotNull(role);
  }

  long getId() {
    return id;
  }

  String getEmailAddress() {
    return emailAddress;
  }

  String getDisplayName() {
    return name;
  }

  Role getRole() {
    return role;
  }

  AuthenticationEntity getAuthenticationEntity() {
    return authenticationEntity;
  }
}
