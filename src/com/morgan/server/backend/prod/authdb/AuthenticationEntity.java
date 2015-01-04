package com.morgan.server.backend.prod.authdb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Entity type for representing authentication information for a user (i.e., email address and
 * password).
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Entity(name = "password")
public class AuthenticationEntity {

  @Id
  private long id;

  @Column(length = 256, nullable = false)
  private String password;

  AuthenticationEntity() {
  }

  AuthenticationEntity(long id, String password) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(password));
    this.id = id;
    this.password = password;
  }

  long getId() {
    return id;
  }

  String getPassword() {
    return password;
  }
}
