package com.morgan.server.mtg;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;

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
  private final CardType cardClassification;
  private final ImmutableSet<String> subTypes;
  private final Rarity rarity;
  private final TextWithSymbols text;

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
      CardType cardClassification,
      ImmutableSet<String> subTypes,
      Rarity rarity,
      TextWithSymbols text) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(name));

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

    Preconditions.checkArgument(allNames.contains(name));
  }

  public Optional<String> getMultiverseId() {
    return multiverseId;
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

  public CardType getCardClassification() {
    return cardClassification;
  }

  public ImmutableSet<String> getSubTypes() {
    return subTypes;
  }

  public Rarity getRarity() {
    return rarity;
  }

  public TextWithSymbols getText() {
    return text;
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
        && text.equals(other.text);
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

    private Optional<String> multiverseId = Optional.empty();
    private String name;
    private CardLayout cardLayout;
    @Nullable private Integer convertedManaCost = null;
    private String cardType;
    private CardType cardClassification;
    private Rarity rarity;
    private TextWithSymbols text;

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
          cardClassification,
          ImmutableSet.copyOf(subTypes),
          Preconditions.checkNotNull(rarity),
          Preconditions.checkNotNull(text));
    }
  }
}
