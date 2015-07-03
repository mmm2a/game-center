package com.morgan.server.mtg;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;


/**
 * Describes the types of Mana symbols available.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class ManaSymbol implements Serializable {

  static final long serialVersionUID = 0L;

  // Empty means colorless
  private final ImmutableSet<ManaColor> colors;
  private final boolean isPhyrexian;
  private final boolean isSnow;

  // If count is absent, then its "X" mana
  @Nullable private final Integer count;

  private ManaSymbol(
      ImmutableSet<ManaColor> colors,
      boolean isPhyrexian,
      boolean isSnow,
      Optional<Integer> count) {

    Preconditions.checkArgument(count.orElse(0) >= 0);
    Preconditions.checkArgument(
        colors.size() <= 2, "Colors must be of size <= 2, but was %s", colors);

    this.colors = ImmutableSet.copyOf(colors);
    this.isPhyrexian = isPhyrexian;
    this.isSnow = isSnow;
    this.count = count.orElse(null);

    // A couple of combinations that aren't allowed
    Preconditions.checkState(
        count.isPresent() || colors.isEmpty(), "For X mana, no color is allowed");
    Preconditions.checkState(colors.isEmpty() || count.get() > 0,
        "Can't have a mana count of 0 and a color");
    Preconditions.checkState(
        !isPhyrexian || !isSnow, "A mana symbol can't be both phyrexian and snow");
    Preconditions.checkState(
        colors.size() < 2 || !isPhyrexian, "A phyrexian mana symbol can't be multi-colored");
    Preconditions.checkState(!isSnow || colors.isEmpty(), "Snow mana is colorless");
  }

  public ManaSymbol(ManaColor simpleColor) {
    this(ImmutableSet.of(simpleColor), false, false, Optional.of(1));
  }

  public ManaSymbol(ManaColor color1, ManaColor color2) {
    this(ImmutableSet.of(color1, color2), false, false, Optional.of(1));
  }

  public ManaSymbol(int count) {
    this(ImmutableSet.of(), false, false, Optional.of(count));
  }

  public ImmutableSet<ManaColor> getColors() {
    return colors;
  }

  public boolean isPhyrexian() {
    return isPhyrexian;
  }

  public boolean isSnow() {
    return isSnow;
  }

  /**
   * Returns the number of this mana symbol, or {@link Optional#empty()} if its "X" mana.
   */
  public Optional<Integer> getCount() {
    return Optional.ofNullable(count);
  }

  public ManaSymbol makeSnow(boolean isSnow) {
    return new ManaSymbol(colors, isPhyrexian, isSnow, getCount());
  }

  public ManaSymbol makePhyrexian(boolean isPhyrexian) {
    return new ManaSymbol(colors, isPhyrexian, isSnow, getCount());
  }

  @Override public int hashCode() {
    return Objects.hash(colors, isPhyrexian, isSnow, count);
  }

  @Override public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof ManaSymbol)) {
      return false;
    }

    ManaSymbol other = (ManaSymbol) o;
    return colors.equals(other.colors)
        && isPhyrexian == other.isPhyrexian
        && isSnow == other.isSnow
        && Objects.equals(count, other.count);
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(ManaSymbol.class)
        .add("colors", colors)
        .add("isPhyrexian", isPhyrexian)
        .add("isSnow", isSnow)
        .add("count", count)
        .toString();
  }

  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder class for building {@link ManaSymbol} instances.
   */
  public static class Builder {

    private final Set<ManaColor> manaColors = new HashSet<>();

    private Integer count = 1;
    private boolean isPhyrexian = false;
    private boolean isSnow = false;

    private Builder() {
    }

    public Builder clearCount() {
      this.count = null;
      return this;
    }

    public Builder setCount(int count) {
      Preconditions.checkArgument(count >= 0);
      this.count = count;
      return this;
    }

    public Builder setIsSnow(boolean isSnow) {
      this.isSnow = isSnow;
      return this;
    }

    public Builder setIsPhyrexian(boolean isPhyrexian) {
      this.isPhyrexian = isPhyrexian;
      return this;
    }

    public Builder addManaColor(ManaColor manaColor) {
      manaColors.add(Preconditions.checkNotNull(manaColor));
      return this;
    }

    public ManaSymbol build() {
      return new ManaSymbol(
          ImmutableSet.copyOf(manaColors), isPhyrexian, isSnow, Optional.ofNullable(count));
    }
  }
}
