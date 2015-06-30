package com.morgan.server.backend.prod.mtgdb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Database representation of "another name" that a card is known by.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Entity(name = "mtgcardname")
class CardNameEntity {

  @Id @GeneratedValue
  private long id;

  @Column(length = 256, nullable = false, unique = false)
  private String name;

  CardNameEntity() {
  }

  CardNameEntity(String name) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
    this.name = name;
  }

  long getId() {
    return id;
  }

  String getName() {
    return name;
  }
}
