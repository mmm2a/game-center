package com.morgan.server.backend.prod.mtgdb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.morgan.server.mtg.CardLayout;
import com.morgan.server.mtg.ManaSymbol;
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

  @Enumerated(EnumType.STRING)
  @Column(length = 32, nullable = false, unique = false)
  private CardLayout cardLayout;

  @Column(length = 128, nullable = false, unique = false)
  private String manaSymbols;

  @OneToOne(cascade = { CascadeType.ALL })
  @JoinColumn(name = "image", nullable = true)
  @Nullable private CardImageEntity cardImageEntity;

  @OneToMany(cascade = { CascadeType.ALL}, fetch = FetchType.LAZY)
  @JoinColumn(name = "allnames", nullable = true)
  @Nullable Collection<CardNameEntity> allCardNames;

  CardEntity() {
  }

  CardEntity(
      ManaSymbolsRepresentation manaSymbolsRepresentation,
      Card rawCard,
      @Nullable CardImageEntity cardImageEntity) {
    multiverseId = rawCard.getMultiverseId().orElse(null);
    name = rawCard.getName();
    cardLayout = rawCard.getCardLayout();
    this.cardImageEntity = cardImageEntity;
    this.manaSymbols = manaSymbolsRepresentation.convert(rawCard.getManaSymbols());

    allCardNames = generateAllNamesEntities(rawCard);
  }

  private Collection<CardNameEntity> generateAllNamesEntities(Card rawCard) {
    List<CardNameEntity> entities = new ArrayList<>();
    for (String name : rawCard.getAllNames()) {
      entities.add(new CardNameEntity(name));
    }
    return entities;
  }

  long getCardId() {
    return cardId;
  }

  String getName() {
    return name;
  }

  ImmutableSet<String> getAllNames() {
    ImmutableSet.Builder<String> resultBuilder = ImmutableSet.builder();
    if (allCardNames != null) {
      for (CardNameEntity entity : allCardNames) {
        resultBuilder.add(entity.getName());
      }
    }
    return resultBuilder.build();
  }

  Optional<String> getMultiverseId() {
    return Optional.ofNullable(multiverseId);
  }

  public ImmutableCollection<ManaSymbol> getManaSymbols(ManaSymbolsRepresentation rep) {
    return ImmutableList.copyOf(rep.reverse().convert(manaSymbols));
  }
}
