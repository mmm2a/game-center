package com.morgan.server.backend.prod.mtgdb;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * JPA entity for a card sub type.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Entity(name = "mtgcardsubtype")
class CardSubTypeEntity implements HasId {

  @Id @GeneratedValue
  private long id;

  @Column(length = 64, unique = true, nullable = false)
  private String subType;

  @ManyToMany(mappedBy = "subTypes")
  private Set<CardEntity> cardEntities;

  CardSubTypeEntity() {
  }

  CardSubTypeEntity(String subType) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(subType));
    this.subType = subType;
    this.cardEntities = new HashSet<>();
  }

  @Override public long getId() {
    return id;
  }

  String getSubType() {
    return subType;
  }

  void addCardEntity(CardEntity entity) {
    this.cardEntities.add(entity);
  }
}
