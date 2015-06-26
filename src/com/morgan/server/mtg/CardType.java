package com.morgan.server.mtg;

import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

/**
 * The various types of cards.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public enum CardType {

  TRIBAL_ARTIFACT(CardTypeComponent.TRIBAL, CardTypeComponent.ARTIFACT),
  TRIBAL_ENCHANTMENT(CardTypeComponent.TRIBAL, CardTypeComponent.ENCHANTMENT),
  TRIBAL_INSTANT(CardTypeComponent.TRIBAL, CardTypeComponent.INSTANT),
  TRIBAL_SORCERY(CardTypeComponent.TRIBAL, CardTypeComponent.SORCERY),
  SCARIEST_CREATURE_YOULL_EVER_SEE(
      CardTypeComponent.SCARIEST,
      CardTypeComponent.CREATURE,
      CardTypeComponent.YOULL,
      CardTypeComponent.EVER,
      CardTypeComponent.SEE),
  CONSPIRACY(CardTypeComponent.CONSPIRACY),
  CREATURE(CardTypeComponent.CREATURE),
  SCHEME(CardTypeComponent.SCHEME),
  SUMMON(CardTypeComponent.SUMMON),
  ARTIFACT_ENCHANTMENT(CardTypeComponent.ARTIFACT, CardTypeComponent.ENCHANTMENT),
  EATURECRAY(CardTypeComponent.EATURECRAY),
  ENCHANT_CREATURE(CardTypeComponent.ENCHANT, CardTypeComponent.CREATURE),
  SORCERY(CardTypeComponent.SORCERY),
  PLANE(CardTypeComponent.PLANE),
  INSTANT(CardTypeComponent.INSTANT),
  ARTIFACT_CREATURE(CardTypeComponent.ARTIFACT, CardTypeComponent.CREATURE),
  LAND_CREATURE(CardTypeComponent.LAND, CardTypeComponent.CREATURE),
  ARTIFACT(CardTypeComponent.ARTIFACT),
  INTERRUPT(CardTypeComponent.INTERRUPT),
  LAND(CardTypeComponent.LAND),
  PLAYER_ENCHANT(CardTypeComponent.PLAYER, CardTypeComponent.ENCHANT),
  CREATURE_ENCHANTMENT(CardTypeComponent.CREATURE, CardTypeComponent.ENCHANTMENT),
  PLANESWALKER(CardTypeComponent.PLANESWALKER),
  PHENOMENON(CardTypeComponent.PHENOMENON),
  VANGUARD(CardTypeComponent.VANGUARD),
  ENCHANTMENT(CardTypeComponent.ENCHANTMENT),
  ARTIFACT_LAND(CardTypeComponent.ARTIFACT, CardTypeComponent.LAND);

  /**
   * Represents a single component (which can be called out in a card's effects) in the totality of
   * a card type.
   */
  public enum CardTypeComponent {
    TRIBAL,
    SEE,
    SCARIEST,
    YOULL,
    EVER,
    CONSPIRACY,
    CREATURE,
    SCHEME,
    SUMMON,
    ENCHANTMENT,
    EATURECRAY,
    ENCHANT,
    SORCERY,
    PLANE,
    INSTANT,
    ARTIFACT,
    INTERRUPT,
    LAND,
    PLAYER,
    PLANESWALKER,
    PHENOMENON,
    VANGUARD;
  }

  private final ImmutableSet<CardTypeComponent> rawComponents;

  private CardType(CardTypeComponent requiredComponent, CardTypeComponent...otherComponents) {
    rawComponents = ImmutableSet.<CardTypeComponent>builder()
        .add(Preconditions.checkNotNull(requiredComponent))
        .addAll(ImmutableSet.copyOf(otherComponents))
        .build();
  }

  public ImmutableSet<CardTypeComponent> getRawComponents() {
    return rawComponents;
  }

  public static Function<Set<CardTypeComponent>, CardType> getComponentToCardTypeFunction() {
    return new Function<Set<CardTypeComponent>, CardType>() {
      @Override @Nullable public CardType apply(Set<CardTypeComponent> t) {
        ImmutableSet<CardTypeComponent> test = ImmutableSet.copyOf(t);
        for (CardType cardType : CardType.values()) {
          if (test.equals(cardType.getRawComponents())) {
            return cardType;
          }
        }

        return null;
      }
    };
  }
}
