package com.morgan.server.backend.prod.mtgdb;

import java.util.Collection;

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
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.morgan.server.mtg.CardLayout;

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

  @ManyToMany(cascade = { CascadeType.ALL}, fetch = FetchType.LAZY)
  @Nullable Collection<CardNameEntity> allCardNames;

  @Column(nullable = true)
  @Nullable private Integer convertedManaCost;

  @Column(length = 8, nullable = false)
  private String colors;

  @Column(length = 64, nullable = true)
  @Nullable private String cardType;

  CardEntity() {
  }

  CardEntity(
      String name,
      @Nullable String multiverseId,
      CardLayout cardLayout,
      String manaSymbols,
      @Nullable CardImageEntity cardImage,
      Iterable<CardNameEntity> allCardNames,
      @Nullable Integer convertedManaCost,
      String colors,
      @Nullable String cardType) {

    Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
    Preconditions.checkNotNull(cardLayout);
    Preconditions.checkNotNull(manaSymbols);
    Preconditions.checkNotNull(allCardNames);
    Preconditions.checkNotNull(colors);

    this.name = name;
    this.multiverseId = multiverseId;
    this.cardLayout = cardLayout;
    this.manaSymbols = manaSymbols;
    this.cardImageEntity = cardImage;
    this.allCardNames = ImmutableSet.copyOf(allCardNames);
    this.convertedManaCost = convertedManaCost;
    this.colors = colors;
    this.cardType = Strings.isNullOrEmpty(cardType) ? null : cardType;
  }

  long getCardId() {
    return cardId;
  }

  String getName() {
    return name;
  }

  Iterable<CardNameEntity> getAllNames() {
    return (allCardNames == null) ? ImmutableSet.of() : allCardNames;
  }

  @Nullable String getMultiverseId() {
    return multiverseId;
  }

  String getManaSymbols() {
    return manaSymbols;
  }

  @Nullable CardImageEntity getCardImage() {
    return cardImageEntity;
  }

  CardLayout getCardLayout() {
    return cardLayout;
  }

  @Nullable Integer getConvertedManaCost() {
    return convertedManaCost;
  }

  @Nullable String getCardType() {
    return cardType;
  }
}
