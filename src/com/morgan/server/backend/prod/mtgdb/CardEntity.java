package com.morgan.server.backend.prod.mtgdb;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.morgan.server.mtg.BorderColor;
import com.morgan.server.mtg.CardLayout;
import com.morgan.server.mtg.CardType;
import com.morgan.server.mtg.Rarity;
import com.morgan.server.mtg.TextWithSymbols;
import com.morgan.server.mtg.Watermark;

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
  @Nullable Set<CardNameEntity> allCardNames;

  @Column(nullable = true)
  @Nullable private Integer convertedManaCost;

  @Column(length = 8, nullable = false)
  private String colors;

  @Column(length = 64, nullable = true)
  @Nullable private String cardType;

  @ManyToMany(cascade = { CascadeType.ALL}, fetch = FetchType.LAZY)
  private Set<CardSuperTypeEntity> cardSuperTypes;

  @Column(length = 32, nullable = true, unique = false)
  @Enumerated(EnumType.STRING)
  @Nullable private CardType cardClassification;

  @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
  private Set<CardSubTypeEntity> subTypes;

  @Enumerated(EnumType.STRING)
  private Rarity rarity;

  @Lob
  @Column(length = 2048, nullable = true)
  @Basic(fetch = FetchType.LAZY)
  @Nullable private TextWithSymbols text;

  @Column(length = 512, nullable = true, unique = false)
  @Nullable private String flavor;

  @Column(length = 16, nullable = true, unique = false)
  @Nullable private String number;

  @Column(length = 64, nullable = false, unique = false)
  private String artist;

  @Column(length = 8, nullable = true, unique = false)
  @Nullable private String power;

  @Column(length = 8, nullable = true, unique = false)
  @Nullable private String toughness;

  @Column(nullable = true, unique = false)
  @Nullable private Integer loyalty;

  @Column(length = 64, nullable = false, unique = false)
  private String alternateMultiverseIds;

  @Column(nullable = true, unique = false)
  @Enumerated(EnumType.STRING)
  @Nullable BorderColor borderColor;

  @Column(nullable = true, unique = false)
  @Enumerated(EnumType.STRING)
  @Nullable Watermark watermark;

  @Column(nullable = false, unique = false)
  boolean isTimeShifted;

  @Column(nullable = true, unique = false)
  @Nullable Integer handModifier;

  @Column(nullable = true, unique = false)
  @Nullable Integer lifeModifier;

  @Column(nullable = false, unique = false)
  boolean isReserved;

  @Column(nullable = false, unique = false)
  boolean isStarter;

  @Column(length = 12, nullable = true)
  @Nullable private String releaseDate;

  CardEntity() {
  }

  CardEntity(
      String name,
      @Nullable String multiverseId,
      CardLayout cardLayout,
      String manaSymbols,
      @Nullable CardImageEntity cardImage,
      @Nullable Integer convertedManaCost,
      String colors,
      @Nullable String cardType,
      @Nullable CardType cardClassification,
      Rarity rarity,
      @Nullable TextWithSymbols text,
      @Nullable String flavor,
      @Nullable String number,
      String artist,
      @Nullable String power,
      @Nullable String toughness,
      @Nullable Integer loyalty,
      String alternateMultiverseIds,
      @Nullable BorderColor borderColor,
      @Nullable Watermark watermark,
      boolean isTimeShifted,
      @Nullable Integer handModifier,
      @Nullable Integer lifeModifier,
      boolean isReserved,
      boolean isStarter,
      @Nullable String releaseDate) {

    Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
    Preconditions.checkNotNull(cardLayout);
    Preconditions.checkNotNull(manaSymbols);
    Preconditions.checkNotNull(colors);
    Preconditions.checkNotNull(rarity);
    Preconditions.checkArgument(!Strings.isNullOrEmpty(artist));
    Preconditions.checkNotNull(alternateMultiverseIds);

    this.name = name;
    this.multiverseId = multiverseId;
    this.cardLayout = cardLayout;
    this.manaSymbols = manaSymbols;
    this.cardImageEntity = cardImage;
    this.allCardNames = new HashSet<>();
    this.convertedManaCost = convertedManaCost;
    this.colors = colors;
    this.cardType = Strings.isNullOrEmpty(cardType) ? null : cardType;
    this.cardSuperTypes = new HashSet<>();
    this.cardClassification = cardClassification;
    this.subTypes = new HashSet<>();
    this.rarity = rarity;
    this.text = text;
    this.flavor = flavor;
    this.number = number;
    this.artist = artist;
    this.power = power;
    this.toughness = toughness;
    this.loyalty = loyalty;
    this.alternateMultiverseIds = alternateMultiverseIds;
    this.borderColor = borderColor;
    this.watermark = watermark;
    this.isTimeShifted = isTimeShifted;
    this.handModifier = handModifier;
    this.lifeModifier = lifeModifier;
    this.isReserved = isReserved;
    this.isStarter = isStarter;
    this.releaseDate = releaseDate;
  }

  long getCardId() {
    return cardId;
  }

  String getName() {
    return name;
  }

  void addToAllNames(CardNameEntity cardName) {
    allCardNames.add(cardName);
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

  void addToCardSuperTypes(CardSuperTypeEntity cardSuperType) {
    this.cardSuperTypes.add(cardSuperType);
  }

  Iterable<CardSuperTypeEntity> getCardSuperTypes() {
    return cardSuperTypes;
  }

  @Nullable CardType getCardClassification() {
    return cardClassification;
  }

  void addCardSubType(CardSubTypeEntity subType) {
    this.subTypes.add(subType);
  }

  Iterable<CardSubTypeEntity> getCardSubTypes() {
    return subTypes;
  }

  Rarity getRarity() {
    return rarity;
  }

  @Nullable TextWithSymbols getText() {
    return text;
  }

  @Nullable String getFlavor() {
    return flavor;
  }

  @Nullable String getNumber() {
    return number;
  }

  String getArtist() {
    return artist;
  }

  @Nullable String getPower() {
    return power;
  }

  @Nullable String getToughness() {
    return toughness;
  }

  @Nullable Integer getLoyalty() {
    return loyalty;
  }

  /**
   * Returns a comma-separate list of alternate multiverse ids.
   */
  String getAlternateMultiverseIds() {
    return alternateMultiverseIds;

  }

  BorderColor getBorderColorOverride() {
    return borderColor;
  }

  Watermark getWatermark() {
    return watermark;
  }

  boolean isTimeShifted() {
    return isTimeShifted;
  }

  Integer getHandModifier() {
    return handModifier;
  }

  Integer getLifeModifier() {
    return lifeModifier;
  }

  boolean isReserved() {
    return isReserved;
  }

  boolean isStarter() {
    return isStarter;
  }

  /**
   * Retrieves the release date as either:
   * <ul>
   *   <li>####<i> - for year only</i>
   *   <li>####-##<i> - for year and month only</li>
   *   <li>####-##-##<i> - for year-month-day</li>
   * </ul>
   */
  @Nullable String getReleaseDate() {
    return releaseDate;
  }
}
