package com.morgan.server.backend.prod.mtgdb;

import java.util.Optional;

import javax.annotation.Nullable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.morgan.server.mtg.raw.Card;

/**
 * Database entity for storing information about a card.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Entity(name = "mtgcard")
class CardEntity {

  @Id @GeneratedValue
  private long cardId;

  @Column(length = 256, nullable = false, unique = false)
  private String name;

  @Column(length = 32, nullable = true, unique = false)
  @Nullable private String multiverseId;

  @OneToOne(cascade = { CascadeType.ALL })
  @JoinColumn(name = "image", nullable = true)
  @Nullable private CardImageEntity cardImageEntity;

  CardEntity() {
  }

  CardEntity(Card rawCard, @Nullable CardImageEntity cardImageEntity) {
    multiverseId = rawCard.getMultiverseId().orElse(null);
    name = rawCard.getName();
    this.cardImageEntity = cardImageEntity;
  }

  long getCardId() {
    return cardId;
  }

  String getName() {
    return name;
  }

  Optional<String> getMultiverseId() {
    return Optional.ofNullable(multiverseId);
  }
}
