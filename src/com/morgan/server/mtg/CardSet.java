package com.morgan.server.mtg;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

import org.joda.time.LocalDate;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;


/**
 * Represents a set of cards in Magic the Gathering.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class CardSet {

  // Name of the set
  private final String name;

  // Code name of the set
  private final String code;

  // Gatherer code -- only present if different from code
  private final Optional<String> gathererCode;

  // Old style code used by some games. Only present if different from gathererCode and code.
  private final Optional<String> oldCode;

  // Code used in magiccards.info. Only present if found in magiccards.info
  private final Optional<String> magicCardsInfoCode;

  private final LocalDate releaseDate;
  private final BorderColor borderColor;
  private final SetType setType;

  // The block this set is in.
  private final Optional<String> block;

  private final boolean onlineOnly;

  // Only available for sets in "gatherer"
  private final Optional<ImmutableSet<Locale>> languagesPrinted;

  private final ImmutableList<Card> cards;

  private CardSet(
      String name,
      String code,
      Optional<String> gathererCode,
      Optional<String> oldCode,
      Optional<String> magicCardsInfoCode,
      LocalDate releaseDate,
      BorderColor borderColor,
      SetType setType,
      Optional<String> block,
      boolean onlineOnly,
      Optional<ImmutableSet<Locale>> languagesPrinted,
      ImmutableList<Card> cards) {
    this.name = name;
    this.code = code;
    this.gathererCode = gathererCode;
    this.oldCode = oldCode;
    this.magicCardsInfoCode = magicCardsInfoCode;
    this.releaseDate = releaseDate;
    this.borderColor = borderColor;
    this.setType = setType;
    this.block = block;
    this.onlineOnly = onlineOnly;
    this.languagesPrinted = languagesPrinted;
    this.cards = cards;
  }

  public String getName() {
    return name;
  }

  public String getCode() {
    return code;
  }

  public Optional<String> getGathererCode() {
    return gathererCode;
  }

  public Optional<String> getOldCode() {
    return oldCode;
  }

  public Optional<String> getMagicCardsInfoCode() {
    return magicCardsInfoCode;
  }

  public LocalDate getReleaseDate() {
    return releaseDate;
  }

  public BorderColor getBorderColor() {
    return borderColor;
  }

  public SetType getSetType() {
    return setType;
  }

  public Optional<String> getBlock() {
    return block;
  }

  public boolean isOnlineOnly() {
    return onlineOnly;
  }

  public Optional<ImmutableSet<Locale>> getLanguagesPrinted() {
    return languagesPrinted;
  }

  public ImmutableList<Card> getCards() {
    return cards;
  }

  @Override public int hashCode() {
    return Objects.hash(name, code);
  }

  @Override public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof CardSet)) {
      return false;
    }

    CardSet other = (CardSet) o;
    return name.equals(other.name)
        && code.equals(other.code)
        && gathererCode.equals(other.gathererCode)
        && oldCode.equals(other.oldCode)
        && magicCardsInfoCode.equals(other.magicCardsInfoCode)
        && releaseDate.equals(other.releaseDate)
        && borderColor == other.borderColor
        && setType == other.setType
        && block.equals(other.block)
        && onlineOnly == other.onlineOnly
        && languagesPrinted.equals(other.languagesPrinted)
        && cards.equals(other.cards);
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(CardSet.class)
        .add("name", name)
        .add("code", code)
        .add("gathererCode", gathererCode)
        .add("oldCode", oldCode)
        .add("magicCardsInfoCode", magicCardsInfoCode)
        .add("releaseDate", releaseDate)
        .add("borderColor", borderColor)
        .add("setType", setType)
        .add("block", block)
        .add("onlineOnly", onlineOnly)
        .add("languagesPrinted", languagesPrinted)
        .add("cards", "...")
        .toString();
  }

  public static Builder builderFor(String name, String code) {
    return new Builder(name, code);
  }

  /**
   * Builder class for building {@link CardSet} instances.
   */
  public static class Builder {

    private final String name;
    private final String code;

    private final ImmutableList.Builder<Card> cardsBuilder = ImmutableList.builder();

    private Optional<String> gathererCode = Optional.empty();
    private Optional<String> oldCode = Optional.empty();
    private Optional<String> magicCardsInfoCode = Optional.empty();
    private LocalDate releaseDate;
    private BorderColor borderColor;
    private SetType setType;
    private Optional<String> block = Optional.empty();
    private boolean onlineOnly = false;
    private Optional<ImmutableSet<Locale>> languagesPrinted = Optional.empty();

    private Builder(String name, String code) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
      Preconditions.checkArgument(!Strings.isNullOrEmpty(code));

      this.name = name;
      this.code = code;
    }

    public Builder setGathererCode(@Nullable String gathererCode) {
      this.gathererCode = Optional.ofNullable(gathererCode);
      return this;
    }

    public Builder setOldCode(@Nullable String oldCode) {
      this.oldCode = Optional.ofNullable(oldCode);
      return this;
    }

    public Builder setMagicCardsInfoCode(@Nullable String magicCardsInfoCode) {
      this.magicCardsInfoCode = Optional.ofNullable(magicCardsInfoCode);
      return this;
    }

    public Builder setReleaseDate(LocalDate releaseDate) {
      this.releaseDate = Preconditions.checkNotNull(releaseDate);
      return this;
    }

    public Builder setBorderColor(BorderColor borderColor) {
      this.borderColor = Preconditions.checkNotNull(borderColor);
      return this;
    }

    public Builder setSetType(SetType setType) {
      this.setType = Preconditions.checkNotNull(setType);
      return this;
    }

    public Builder setBlock(@Nullable String block) {
      this.block = Optional.ofNullable(block);
      return this;
    }

    public Builder setOnlineOnly(boolean isOnlineOnly) {
      this.onlineOnly = isOnlineOnly;
      return this;
    }

    public Builder setLanguagesPrinted(@Nullable Iterable<Locale> languagesPrinted) {
      ImmutableSet<Locale> printed = null;
      if (languagesPrinted != null) {
        printed = ImmutableSet.copyOf(languagesPrinted);
      }
      this.languagesPrinted = Optional.ofNullable(printed);
      return this;
    }

    public Builder addCard(Card card) {
      cardsBuilder.add(card);
      return this;
    }

    public CardSet build() {
      Preconditions.checkState(releaseDate != null);
      Preconditions.checkState(borderColor != null);
      Preconditions.checkState(setType != null);
      Preconditions.checkState(block != null);

      return new CardSet(
          name,
          code,
          gathererCode,
          oldCode,
          magicCardsInfoCode,
          releaseDate,
          borderColor,
          setType,
          block,
          onlineOnly,
          languagesPrinted,
          cardsBuilder.build());
    }
  }
}
