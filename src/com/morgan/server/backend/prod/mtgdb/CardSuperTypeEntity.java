package com.morgan.server.backend.prod.mtgdb;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.google.common.base.Preconditions;
import com.morgan.server.mtg.CardSuperType;

/**
 * Entity class for storing card super-types.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Entity(name = "mtgcardsupertype")
@NamedQueries({
  @NamedQuery(name = "mtgLookupCardSuperType",
      query = "SELECT s FROM mtgcardsupertype AS s WHERE s.cardSuperType = :superType")
})
class CardSuperTypeEntity implements HasId {

  @Id @GeneratedValue
  private long id;

  @Column(length = 32, nullable = false, unique = true)
  @Enumerated(EnumType.STRING)
  private CardSuperType cardSuperType;

  @ManyToMany(mappedBy = "cardSuperTypes")
  private Set<CardEntity> cardEntities;

  CardSuperTypeEntity() {
  }

  CardSuperTypeEntity(CardSuperType cardSuperType) {
    this.cardSuperType = Preconditions.checkNotNull(cardSuperType);
    cardEntities = new HashSet<>();
  }

  CardSuperType getCardSuperType() {
    return cardSuperType;
  }

  @Override public long getId() {
    return id;
  }

  void addCardEntity(CardEntity cardEntity) {
    this.cardEntities.add(cardEntity);
  }
}
