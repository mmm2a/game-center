package com.morgan.server.mtg;

import java.util.Locale;
import java.util.Objects;

import org.joda.time.LocalDate;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

/**
 * Represents extra information about a card.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class CardExtraInformation {

  private final ImmutableMultimap<LocalDate, TextWithSymbols> rulings;
  private final ImmutableMultimap<Locale, String> foreignNames;

  private CardExtraInformation(
      Multimap<LocalDate, TextWithSymbols> rulings,
      Multimap<Locale, String> foreignNames) {
    this.rulings = ImmutableMultimap.copyOf(rulings);
    this.foreignNames = ImmutableMultimap.copyOf(foreignNames);
  }

  /**
   * Returns a {@link Multimap} mapping {@link LocalDate} instances describing when a ruling
   * occurred to a textual representation of that ruling.
   */
  public ImmutableMultimap<LocalDate, TextWithSymbols> getRulings() {
    return rulings;
  }

  public ImmutableMultimap<Locale, String> getForeignNames() {
    return foreignNames;
  }

  @Override public int hashCode() {
    return Objects.hash(rulings);
  }

  @Override public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof CardExtraInformation)) {
      return false;
    }

    CardExtraInformation other = (CardExtraInformation) o;
    return rulings.equals(other.rulings)
        && foreignNames.equals(other.foreignNames);
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(CardExtraInformation.class)
        .add("rulings", rulings)
        .add("foreignNames", foreignNames)
        .toString();
  }

  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder class for building {@link CardExtraInformation} instances.
   */
  public static class Builder {

    private final ImmutableMultimap.Builder<LocalDate, TextWithSymbols> rulingsBuilder =
        ImmutableMultimap.builder();
    private final ImmutableMultimap.Builder<Locale, String> foreignNamesBuilder =
        ImmutableMultimap.builder();

    private Builder() {
    }

    public Builder addRuling(LocalDate date, TextWithSymbols text) {
      rulingsBuilder.put(Preconditions.checkNotNull(date), Preconditions.checkNotNull(text));
      return this;
    }

    public Builder addForeignName(Locale locale, String foreignName) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(foreignName));
      this.foreignNamesBuilder.put(locale, foreignName);
      return this;
    }

    public CardExtraInformation build() {
      return new CardExtraInformation(
          rulingsBuilder.build(),
          foreignNamesBuilder.build());
    }
  }
}