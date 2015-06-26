package com.morgan.server.mtg.json;

import java.util.Locale;
import java.util.function.Function;

import com.google.common.collect.ImmutableCollection;
import com.google.gson.JsonObject;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.morgan.server.mtg.BoosterType;
import com.morgan.server.mtg.BorderColor;
import com.morgan.server.mtg.Card;
import com.morgan.server.mtg.CardLayout;
import com.morgan.server.mtg.CardSet;
import com.morgan.server.mtg.CardSuperType;
import com.morgan.server.mtg.CardType.CardTypeComponent;
import com.morgan.server.mtg.ManaColor;
import com.morgan.server.mtg.ManaSymbol;
import com.morgan.server.mtg.OtherSymbol;
import com.morgan.server.mtg.Rarity;
import com.morgan.server.mtg.SetType;
import com.morgan.server.mtg.TextWithSymbols;

/**
 * GUICE module for the mtg.json package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class JsonModule extends AbstractModule {

  @Override protected void configure() {
    bind(new TypeLiteral<Function<String, Locale>>() {})
        .annotatedWith(JsonMapping.class)
        .to(JsonLanguagesFunction.class);
    bind(new TypeLiteral<Function<JsonObject, CardSet>>() {})
        .annotatedWith(JsonMapping.class)
        .to(JsonToCardSetFunction.class);
    bind(new TypeLiteral<Function<JsonObject, Card>>() {})
        .annotatedWith(JsonMapping.class)
        .to(JsonToCardFunction.class);
    bind(new TypeLiteral<Function<String, ManaSymbol>>() {})
        .annotatedWith(JsonMapping.class)
        .to(JsonManaSymbolFunction.class);
    bind(new TypeLiteral<Function<String, ImmutableCollection<ManaSymbol>>>() {})
        .annotatedWith(JsonMapping.class)
        .to(JsonManaSymbolsFunction.class);
    bind(new TypeLiteral<Function<String, TextWithSymbols>>() {})
        .annotatedWith(JsonMapping.class)
        .to(JsonTextWithSymbolsConverter.class);
  }

  @Provides @JsonMapping protected Function<String, BorderColor> provideBorderColorJsonMapping() {
    return JsonEnumMapping.builderFor(BorderColor.class)
        .addMapping("black", BorderColor.BLACK)
        .addMapping("white", BorderColor.WHITE)
        .addMapping("silver", BorderColor.SILVER)
        .build();
  }

  @Provides @JsonMapping protected Function<String, SetType> provideSetTypeJsonMapping() {
    return JsonEnumMapping.builderFor(SetType.class)
        .addMapping("conspiracy", SetType.CONSPIRACY)
        .addMapping("core", SetType.CORE)
        .addMapping("expansion", SetType.EXPANSION)
        .addMapping("reprint", SetType.REPRINT)
        .addMapping("box", SetType.BOX)
        .addMapping("un", SetType.UN)
        .addMapping("from the vault", SetType.FROM_THE_VAULT)
        .addMapping("premium deck", SetType.PREMIUM_DECK)
        .addMapping("duel deck", SetType.DUEL_DECK)
        .addMapping("starter", SetType.STARTER)
        .addMapping("commander", SetType.COMMANDER)
        .addMapping("planechase", SetType.PLANECHASE)
        .addMapping("archenemy", SetType.ARCH_ENEMY)
        .addMapping("promo", SetType.PROMO)
        .addMapping("vanguard", SetType.VANGUARD)
        .addMapping("masters", SetType.MASTERS)
        .build();
  }

  @Provides @JsonMapping protected Function<String, BoosterType> provideBoosterTypeJsonMapping() {
    return JsonEnumMapping.builderFor(BoosterType.class)
        .addMapping("power nine", BoosterType.POWER_NINE)
        .addMapping("mythic rare", BoosterType.MYTHIC_RARE)
        .addMapping("timeshifted rare", BoosterType.TIMESHIFTED_RARE)
        .addMapping("urza land", BoosterType.URZA_LAND)
        .addMapping("checklist", BoosterType.CHECKLIST)
        .addMapping("foil mythic rare", BoosterType.FOIL_MYTHIC_RARE)
        .addMapping("timeshifted uncommon", BoosterType.TIMESHIFTED_UNCOMMON)
        .addMapping("foil rare", BoosterType.FOIL_RARE)
        .addMapping("foil uncommon", BoosterType.FOIL_UNCOMMON)
        .addMapping("marketing", BoosterType.MARKETING)
        .addMapping("foil common", BoosterType.FOIL_COMMON)
        .addMapping("uncommon", BoosterType.UNCOMMON)
        .addMapping("double faced", BoosterType.DOUBLE_FACED)
        .addMapping("common", BoosterType.COMMON)
        .addMapping("draft-matters", BoosterType.DRAFT_MATTERS)
        .addMapping("rare", BoosterType.RARE)
        .addMapping("land", BoosterType.LAND)
        .addMapping("timeshifted common", BoosterType.TIMESHIFTED_COMMON)
        .addMapping("timeshifted purple", BoosterType.TIMESHIFTED_PURPLE)
        .addMapping("foil", BoosterType.FOIL)
        .build();
  }

  @Provides @JsonMapping protected Function<String, CardLayout> provideCardLayoutJsonMapping() {
    return JsonEnumMapping.builderFor(CardLayout.class)
        .addMapping("normal", CardLayout.NORMAL)
        .addMapping("split", CardLayout.SPLIT)
        .addMapping("flip", CardLayout.FLIP)
        .addMapping("double-faced", CardLayout.DOUBLE_FACED)
        .addMapping("token", CardLayout.TOKEN)
        .addMapping("plane", CardLayout.PLANE)
        .addMapping("scheme", CardLayout.SCHEME)
        .addMapping("phenomenon", CardLayout.PHENOMENON)
        .addMapping("leveler", CardLayout.LEVELER)
        .addMapping("vanguard", CardLayout.VANGUARD)
        .build();
  }

  @Provides @JsonMapping protected Function<String, ManaColor> provideJsonColorMapping() {
    return JsonEnumMapping.builderFor(ManaColor.class)
        .addMapping("Red", ManaColor.RED)
        .addMapping("White", ManaColor.WHITE)
        .addMapping("Blue", ManaColor.BLUE)
        .addMapping("Black", ManaColor.BLACK)
        .addMapping("Green", ManaColor.GREEN)
        .build();
  }

  @Provides
  @JsonMapping
  protected Function<String, CardSuperType> provideJsonCardSuperTypeMapping() {
    return JsonEnumMapping.builderFor(CardSuperType.class)
        .addMapping("Basic", CardSuperType.BASIC)
        .addMapping("Legendary", CardSuperType.LEGENDARY)
        .addMapping("Snow", CardSuperType.SNOW)
        .addMapping("Ongoing", CardSuperType.ONGOING)
        .addMapping("World", CardSuperType.WORLD)
        .build();
  }

  @Provides
  @JsonMapping
  protected Function<String, CardTypeComponent> provideJsonTypeToComponent() {
    return JsonEnumMapping.builderFor(CardTypeComponent.class)
        .addMapping("Tribal", CardTypeComponent.TRIBAL)
        .addMapping("See", CardTypeComponent.SEE)
        .addMapping("Scariest", CardTypeComponent.SCARIEST)
        .addMapping("You'll", CardTypeComponent.YOULL)
        .addMapping("Ever", CardTypeComponent.EVER)
        .addMapping("Conspiracy", CardTypeComponent.CONSPIRACY)
        .addMapping("Creature", CardTypeComponent.CREATURE)
        .addMapping("Scheme", CardTypeComponent.SCHEME)
        .addMapping("Summon", CardTypeComponent.SUMMON)
        .addMapping("Enchantment", CardTypeComponent.ENCHANTMENT)
        .addMapping("Eaturecray", CardTypeComponent.EATURECRAY)
        .addMapping("Enchant", CardTypeComponent.ENCHANT)
        .addMapping("Sorcery", CardTypeComponent.SORCERY)
        .addMapping("Plane", CardTypeComponent.PLANE)
        .addMapping("Instant", CardTypeComponent.INSTANT)
        .addMapping("Artifact", CardTypeComponent.ARTIFACT)
        .addMapping("Interrupt", CardTypeComponent.INTERRUPT)
        .addMapping("Land", CardTypeComponent.LAND)
        .addMapping("Player", CardTypeComponent.PLAYER)
        .addMapping("Planeswalker", CardTypeComponent.PLANESWALKER)
        .addMapping("Phenomenon", CardTypeComponent.PHENOMENON)
        .addMapping("Vanguard", CardTypeComponent.VANGUARD)
        .build();
  }

  @Provides @JsonMapping protected Function<String, Rarity> provideJsonToRarityMapping() {
    return JsonEnumMapping.builderFor(Rarity.class)
        .addMapping("Uncommon", Rarity.UNCOMMON)
        .addMapping("Rare", Rarity.RARE)
        .addMapping("Basic Land", Rarity.BASIC_LAND)
        .addMapping("Special", Rarity.SPECIAL)
        .addMapping("Mythic Rare", Rarity.MYTHIC_RARE)
        .addMapping("Common", Rarity.COMMON)
        .build();
  }

  @Provides @JsonMapping protected Function<String, OtherSymbol> provideJsonToOtherSymbolMapping() {
    return JsonEnumMapping.builderFor(OtherSymbol.class)
        .addMapping("{T}", OtherSymbol.TAP)
        .addMapping("{Q}", OtherSymbol.UNTAP)
        .addMapping("{C}", OtherSymbol.CHAOS)
        .build();
  }
}
