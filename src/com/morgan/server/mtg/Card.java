package com.morgan.server.mtg;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;

import org.joda.time.LocalDate;
import org.joda.time.Partial;
import org.joda.time.ReadablePartial;
import org.joda.time.YearMonth;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

/**
 * Represents an individual Magic the Gathering card.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class Card {

  private final Optional<String> multiverseId;

  private final String name;
  private final CardLayout cardLayout;

  // Stores all the names that this card is known by (including its own).  Used for split, flip, and
  // double-faced (though it will always contain at least its own name).
  private final ImmutableSet<String> allNames;

  private final ImmutableCollection<ManaSymbol> manaSymbols;
  private final Optional<Integer> convertedManaCost;
  private final ImmutableSet<ManaColor> colors;

  private final String cardType;
  private final ImmutableSet<CardSuperType> cardSuperTypes;
  private final Optional<CardType> cardClassification;
  private final ImmutableSet<String> subTypes;
  private final Rarity rarity;
  private final Optional<TextWithSymbols> text;

  private final Optional<String> flavor;
  private final Optional<String> number;
  private final String artist;

  private final Optional<String> power;
  private final Optional<String> toughness;
  private final Optional<Integer> loyalty;

  private final ImmutableSet<String> alternateMultiverseIds;

  private final Optional<BorderColor> borderColorOverride;
  private final Optional<Watermark> watermark;

  private final boolean timeShifted;
  private final Optional<Integer> handModifier;
  private final Optional<Integer> lifeModifier;
  private final boolean reserved;
  private final boolean starter;

  private final Optional<ReadablePartial> releaseDate;

  private final CardExtraInformation cardExtraInformation;

  private Card(
      Optional<String> multiverseId,
      String name,
      CardLayout cardLayout,
      ImmutableSet<String> allNames,
      ImmutableCollection<ManaSymbol> manaSymbols,
      Optional<Integer> convertedManaCost,
      ImmutableSet<ManaColor> colors,
      String cardType,
      ImmutableSet<CardSuperType> cardSuperTypes,
      Optional<CardType> cardClassification,
      ImmutableSet<String> subTypes,
      Rarity rarity,
      Optional<TextWithSymbols> text,
      Optional<String> flavor,
      Optional<String> number,
      String artist,
      Optional<String> power,
      Optional<String> toughness,
      Optional<Integer> loyalty,
      ImmutableSet<String> alternateMultiverseIds,
      Optional<BorderColor> borderColorOverride,
      Optional<Watermark> watermark,
      boolean timeShifted,
      Optional<Integer> handModifier,
      Optional<Integer> lifeModifier,
      boolean reserved,
      boolean starter,
      Optional<ReadablePartial> releaseDate,
      CardExtraInformation cardExtraInformation) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
    Preconditions.checkArgument(!Strings.isNullOrEmpty(artist));

    this.multiverseId = Preconditions.checkNotNull(multiverseId);
    this.name = name;
    this.cardLayout = Preconditions.checkNotNull(cardLayout);
    this.allNames = Preconditions.checkNotNull(allNames);
    this.manaSymbols = Preconditions.checkNotNull(manaSymbols);
    this.convertedManaCost = Preconditions.checkNotNull(convertedManaCost);
    this.colors = Preconditions.checkNotNull(colors);
    this.cardType = Preconditions.checkNotNull(cardType);
    this.cardSuperTypes = Preconditions.checkNotNull(cardSuperTypes);
    this.cardClassification = Preconditions.checkNotNull(cardClassification);
    this.subTypes = Preconditions.checkNotNull(subTypes);
    this.rarity = Preconditions.checkNotNull(rarity);
    this.text = Preconditions.checkNotNull(text);
    this.flavor = Preconditions.checkNotNull(flavor);
    this.number = Preconditions.checkNotNull(number);
    this.artist = artist;
    this.power = Preconditions.checkNotNull(power);
    this.toughness = Preconditions.checkNotNull(toughness);
    this.loyalty = Preconditions.checkNotNull(loyalty);
    this.alternateMultiverseIds = Preconditions.checkNotNull(alternateMultiverseIds);
    this.borderColorOverride = Preconditions.checkNotNull(borderColorOverride);
    this.watermark = Preconditions.checkNotNull(watermark);
    this.timeShifted = timeShifted;
    this.handModifier = Preconditions.checkNotNull(handModifier);
    this.lifeModifier = Preconditions.checkNotNull(lifeModifier);
    this.reserved = reserved;
    this.starter = starter;
    this.releaseDate = Preconditions.checkNotNull(releaseDate);
    this.cardExtraInformation = Preconditions.checkNotNull(cardExtraInformation);

    Preconditions.checkArgument(allNames.contains(name));
  }

  public Optional<String> getMultiverseId() {
    return multiverseId;
  }

  /**
   * Gets the other multiverse ids by which this card is known (i.e., alternative image art).
   */
  public ImmutableSet<String> getAlternateMultiverseIds() {
    return alternateMultiverseIds;
  }

  public String getName() {
    return name;
  }

  public CardLayout getCardLayout() {
    return cardLayout;
  }

  public ImmutableSet<String> getAllNames() {
    return allNames;
  }

  public ImmutableCollection<ManaSymbol> getManaSymbols() {
    return manaSymbols;
  }

  public Optional<Integer> getConvertedManaCost() {
    return convertedManaCost;
  }

  public ImmutableSet<ManaColor> getColors() {
    return colors;
  }

  public String getCardType() {
    return cardType;
  }

  public ImmutableSet<CardSuperType> getCardSuperTypes() {
    return cardSuperTypes;
  }

  public Optional<CardType> getCardClassification() {
    return cardClassification;
  }

  public ImmutableSet<String> getSubTypes() {
    return subTypes;
  }

  public Rarity getRarity() {
    return rarity;
  }

  public Optional<TextWithSymbols> getText() {
    return text;
  }

  public Optional<String> getFlavorText() {
    return flavor;
  }

  public Optional<String> getNumber() {
    return number;
  }

  public String getArtist() {
    return artist;
  }

  public Optional<String> getPower() {
    return power;
  }

  public Optional<String> getToughness() {
    return toughness;
  }

  public Optional<Integer> getLoyalty() {
    return loyalty;
  }

  public Optional<BorderColor> getBorderColorOverride() {
    return borderColorOverride;
  }

  public Optional<Watermark> getWatermark() {
    return watermark;
  }

  public boolean isTimeShifted() {
    return timeShifted;
  }

  /**
   * Gets the hand modifier if any (only for Vanguard).
   */
  public Optional<Integer> getHandModifier() {
    return handModifier;
  }

  /**
   * Gets the life modifier if any (only for Vanguard).
   */
  public Optional<Integer> getLifeModifier() {
    return lifeModifier;
  }

  /**
   * Is this card reserved by Wizards of the Coast for reprint.
   */
  public boolean isReserved() {
    return reserved;
  }

  /**
   * Is this card one that is included in a starter set.
   */
  public boolean isStarter() {
    return starter;
  }

  /**
   * Returns the release date as either a
   * <ul>
   *   <li>{@link LocalDate}
   *   <li>{@link YearMonth}
   *   <li>{@link Partial} with only a year set
   * </ul>
   * or returns absent if no release date is known/appropriate.
   */
  public Optional<ReadablePartial> getReleaseDate() {
    return releaseDate;
  }

  public CardExtraInformation getCardExtraInformation() {
    return cardExtraInformation;
  }

  @Override public int hashCode() {
    return Objects.hash(multiverseId, name);
  }

  @Override public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof Card)) {
      return false;
    }

    Card other = (Card) o;
    return multiverseId.equals(other.multiverseId)
        && name.equals(other.name)
        && cardLayout == other.cardLayout
        && allNames.equals(other.allNames)
        && manaSymbols.equals(manaSymbols)
        && convertedManaCost.equals(convertedManaCost)
        && colors.equals(other.colors)
        && cardType.equals(other.cardType)
        && cardSuperTypes.equals(other.cardSuperTypes)
        && cardClassification == other.cardClassification
        && subTypes.equals(other.subTypes)
        && rarity.equals(other.rarity)
        && text.equals(other.text)
        && flavor.equals(other.flavor)
        && number.equals(other.number)
        && artist.equals(other.artist)
        && power.equals(other.power)
        && toughness.equals(other.toughness)
        && loyalty.equals(other.loyalty)
        && alternateMultiverseIds.equals(other.alternateMultiverseIds)
        && borderColorOverride.equals(other.borderColorOverride)
        && watermark.equals(other.watermark)
        && timeShifted == other.timeShifted
        && handModifier.equals(other.handModifier)
        && lifeModifier.equals(other.lifeModifier)
        && reserved == other.reserved
        && starter == other.starter
        && releaseDate.equals(other.releaseDate)
        && cardExtraInformation.equals(other.cardExtraInformation);
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(Card.class)
        .add("multiverseId", multiverseId)
        .add("name", name)
        .add("cardLayout", cardLayout)
        .add("allNames", allNames)
        .add("manaSymbols", manaSymbols)
        .add("convertedManaCost", convertedManaCost)
        .add("colors", colors)
        .add("cardType", cardType)
        .add("cardSuperTypes", cardSuperTypes)
        .add("cardClassification", cardClassification)
        .add("subTypes", subTypes)
        .add("rarity", rarity)
        .add("text", text)
        .add("flavor", flavor)
        .add("number", number)
        .add("artist", artist)
        .add("power", power)
        .add("toughness", toughness)
        .add("loyalty", loyalty)
        .add("alternateMultiverseIds", alternateMultiverseIds)
        .add("borderColorOverride", borderColorOverride)
        .add("watermark", watermark)
        .add("timeShifted", timeShifted)
        .add("handModifier", handModifier)
        .add("lifeModifier", lifeModifier)
        .add("reserved", reserved)
        .add("starter", starter)
        .add("releaseDate", releaseDate)
        .add("cardExtraInformation", cardExtraInformation)
        .toString();
  }

  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder class for building {@link Card} instances.
   */
  public static class Builder {

    private final Set<String> names = new HashSet<>();
    private final Collection<ManaSymbol> manaSymbols = new LinkedList<>();
    private final Set<ManaColor> colors = new HashSet<>();
    private final Set<CardSuperType> cardSuperTypes = new HashSet<>();
    private final Set<String> subTypes = new HashSet<>();
    private final Set<String> alternateMultiverseIds = new HashSet<>();

    private Optional<String> multiverseId = Optional.empty();
    private String name;
    private CardLayout cardLayout;
    @Nullable private Integer convertedManaCost = null;
    private String cardType;
    @Nullable private CardType cardClassification;
    private Rarity rarity;
    @Nullable private TextWithSymbols text;

    @Nullable private String flavor;
    @Nullable private String number;
    private String artist;

    @Nullable private String power;
    @Nullable private String toughness;
    @Nullable private Integer loyalty;

    @Nullable private BorderColor borderColorOverride;
    @Nullable private Watermark watermark;

    private Boolean timeShifted;
    @Nullable private Integer handModifier;
    @Nullable private Integer lifeModifier;
    private Boolean reserved;
    private Boolean starter;

    @Nullable ReadablePartial releaseDate;

    private CardExtraInformation cardExtraInformation;

    private Builder() {
    }

    public Builder setMultiverseId(String multiverseId) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(multiverseId));
      this.multiverseId = Optional.of(multiverseId);
      return this;
    }

    public Builder setName(String name) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
      this.name = name;
      return this;
    }

    public Builder setCardLayout(CardLayout cardLayout) {
      this.cardLayout = Preconditions.checkNotNull(cardLayout);
      return this;
    }

    public Builder addName(String name) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
      this.names.add(name);
      return this;
    }

    public Builder addManaSymbol(ManaSymbol symbol) {
      this.manaSymbols.add(symbol);
      return this;
    }

    public Builder addManaSymbols(Iterable<ManaSymbol> symbols) {
      Iterables.addAll(this.manaSymbols, symbols);
      return this;
    }

    public Builder setConvertedManaCost(int cmc) {
      Preconditions.checkArgument(cmc >= 0);
      convertedManaCost = cmc;
      return this;
    }

    public Builder addColor(ManaColor color) {
      this.colors.add(Preconditions.checkNotNull(color));
      return this;
    }

    public Builder setCardType(String cardType) {
      Preconditions.checkNotNull(cardType);
      this.cardType = cardType;
      return this;
    }

    public Builder addCardSuperType(CardSuperType cardSuperType) {
      this.cardSuperTypes.add(Preconditions.checkNotNull(cardSuperType));
      return this;
    }

    public Builder setCardClassification(CardType cardClassification) {
      this.cardClassification = Preconditions.checkNotNull(cardClassification);
      return this;
    }

    public Builder addSubType(String subType) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(subType));
      this.subTypes.add(subType);
      return this;
    }

    public Builder setRarity(Rarity rarity) {
      this.rarity = Preconditions.checkNotNull(rarity);
      return this;
    }

    public Builder setText(TextWithSymbols text) {
      this.text = Preconditions.checkNotNull(text);
      return this;
    }

    public Builder setNumber(String number) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(number));
      this.number = number;
      return this;
    }

    public Builder setFlavor(String flavor) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(flavor));
      this.flavor = flavor;
      return this;
    }

    public Builder setArtist(String artist) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(artist));
      this.artist = artist;
      return this;
    }

    public Builder setPower(String power) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(power));
      this.power = power;
      return this;
    }

    public Builder setToughness(String toughness) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(toughness));
      this.toughness = toughness;
      return this;
    }

    public Builder setLoyalty(int loyalty) {
      Preconditions.checkArgument(loyalty > 0);
      this.loyalty = loyalty;
      return this;
    }

    public Builder setBorderColorOverride(BorderColor borderColorOverride) {
      this.borderColorOverride = Preconditions.checkNotNull(borderColorOverride);
      return this;
    }

    public Builder setWatermark(Watermark watermark) {
      this.watermark = Preconditions.checkNotNull(watermark);
      return this;
    }

    public Builder setTimeShifted(boolean isTimeShifted) {
      this.timeShifted = isTimeShifted;
      return this;
    }

    public Builder setHandModifier(int handModifier) {
      this.handModifier = handModifier;
      return this;
    }

    public Builder setLifeModifier(int lifeModifier) {
      this.lifeModifier = lifeModifier;
      return this;
    }

    public Builder setReserved(boolean isReserved) {
      this.reserved = isReserved;
      return this;
    }

    public Builder setStarter(boolean isStarter) {
      this.starter = isStarter;
      return this;
    }

    public Builder addAlternateMultiverseId(String alternateMultiverseId) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(alternateMultiverseId));
      this.alternateMultiverseIds.add(alternateMultiverseId);
      return this;
    }

    public Builder setReleaseDate(ReadablePartial releaseDate) {
      this.releaseDate = Preconditions.checkNotNull(releaseDate);
      return this;
    }

    public Builder setCardExtraInformation(CardExtraInformation cardExtraInformation) {
      this.cardExtraInformation = Preconditions.checkNotNull(cardExtraInformation);
      return this;
    }

    public Card build() {
      // Since we're about to add name to names, let's make sure it was set
      Preconditions.checkState(!Strings.isNullOrEmpty(name));
      return new Card(
          multiverseId,
          name,
          cardLayout,
          ImmutableSet.copyOf(Iterables.concat(names, ImmutableSet.of(name))),
          ImmutableList.copyOf(manaSymbols),
          Optional.ofNullable(convertedManaCost),
          ImmutableSet.copyOf(colors),
          cardType,
          ImmutableSet.copyOf(cardSuperTypes),
          Optional.ofNullable(cardClassification),
          ImmutableSet.copyOf(subTypes),
          Preconditions.checkNotNull(rarity),
          Optional.ofNullable(text),
          Optional.ofNullable(flavor),
          Optional.ofNullable(number),
          Preconditions.checkNotNull(artist),
          Optional.ofNullable(power),
          Optional.ofNullable(toughness),
          Optional.ofNullable(loyalty),
          ImmutableSet.copyOf(alternateMultiverseIds),
          Optional.ofNullable(borderColorOverride),
          Optional.ofNullable(watermark),
          Preconditions.checkNotNull(timeShifted),
          Optional.ofNullable(handModifier),
          Optional.ofNullable(lifeModifier),
          Preconditions.checkNotNull(reserved),
          Preconditions.checkNotNull(starter),
          Optional.ofNullable(releaseDate),
          Preconditions.checkNotNull(cardExtraInformation));
    }
  }
}
