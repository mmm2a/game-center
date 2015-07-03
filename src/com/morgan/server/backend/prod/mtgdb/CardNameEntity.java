package com.morgan.server.backend.prod.mtgdb;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Database representation of "another name" that a card is known by.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Entity(name = "mtgcardname")
@NamedQueries({
  @NamedQuery(name = "mtgLookupName", query = "SELECT n FROM mtgcardname AS n WHERE n.name = :name"),
})
class CardNameEntity implements HasId {

  @Id @GeneratedValue
  private long id;

  @Column(length = 256, nullable = false, unique = true)
  private String name;

  @ManyToMany(mappedBy = "allCardNames")
  private Set<CardEntity> allCards;

  CardNameEntity() {
  }

  CardNameEntity(String name) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
    this.name = name;
    allCards = new HashSet<>();
  }

  @Override public long getId() {
    return id;
  }

  String getName() {
    return name;
  }

  void addToAllCards(CardEntity entity) {
    allCards.add(entity);
  }

  Set<CardEntity> getAllCards() {
    return allCards;
  }
}
