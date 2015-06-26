package com.morgan.server.mtg.json;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.morgan.server.mtg.Card;
import com.morgan.server.mtg.CardLayout;
import com.morgan.server.mtg.CardSuperType;
import com.morgan.server.mtg.CardType;
import com.morgan.server.mtg.CardType.CardTypeComponent;
import com.morgan.server.mtg.ManaColor;
import com.morgan.server.mtg.ManaSymbol;
import com.morgan.server.mtg.Rarity;
import com.morgan.server.mtg.TextWithSymbols;
import com.morgan.server.util.log.AdvancedLogger;
import com.morgan.server.util.log.InjectLogger;

/**
 * Implementation of a {@link Function} that converts a JSON representation of a
 * Magic the Gathering card into a {@link Card}.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class JsonToCardFunction implements Function<JsonObject, Card> {

  private static final Set<String> rarities = new HashSet<>();

  @InjectLogger private AdvancedLogger log = AdvancedLogger.NULL;

  private final JsonTypeVisitState visitState;
  private final Function<String, CardLayout> cardLayoutFunction;
  private final Function<String, ImmutableCollection<ManaSymbol>> jsonManaSymbolsFunction;
  private final Function<String, ManaColor> jsonColorMappingFunction;
  private final Function<String, CardSuperType> jsonCardSuperTypeMappingFunction;
  private final Function<String, CardTypeComponent> jsonCardTypeComponentMappingFunction;
  private final Function<String, Rarity> rarityMapping;
  private final Function<String, TextWithSymbols> textWithSymbolsMapping;

  @Inject JsonToCardFunction(
      JsonTypeVisitState visitState,
      @JsonMapping Function<String, CardLayout> cardLayoutFunction,
      @JsonMapping Function<String, ImmutableCollection<ManaSymbol>> jsonManaSymbolsFunction,
      @JsonMapping Function<String, ManaColor> jsonColorMappingFunction,
      @JsonMapping Function<String, CardSuperType> jsonCardSuperTypeMappingFunction,
      @JsonMapping Function<String, CardTypeComponent> jsonCardTypeComponentMappingFunction,
      @JsonMapping Function<String, Rarity> rarityMapping,
      @JsonMapping Function<String, TextWithSymbols> textWithSymbolsMapping) {
    this.visitState = visitState;
    this.cardLayoutFunction = cardLayoutFunction;
    this.jsonManaSymbolsFunction = jsonManaSymbolsFunction;
    this.jsonColorMappingFunction = jsonColorMappingFunction;
    this.jsonCardSuperTypeMappingFunction = jsonCardSuperTypeMappingFunction;
    this.jsonCardTypeComponentMappingFunction = jsonCardTypeComponentMappingFunction;
    this.rarityMapping = rarityMapping;
    this.textWithSymbolsMapping = textWithSymbolsMapping;
  }

  @Override public Card apply(JsonObject obj) {
    Function<String, JsonElement> accessor = visitState.getAccessorFor("card", obj);
    Card.Builder builder = Card.builder();

    String name = accessor.apply("name").getAsString();
    builder.setName(name);
    builder.setCardLayout(cardLayoutFunction.apply(accessor.apply("layout").getAsString()));

    if (obj.has("names")) {
      accessor.apply("names").getAsJsonArray().forEach(e -> builder.addName(e.getAsString()));
    }

    if (obj.has("manaCost")) {
      builder.addManaSymbols(
          jsonManaSymbolsFunction.apply(accessor.apply("manaCost").getAsString()));
    }

    if (obj.has("cmc")) {
      builder.setConvertedManaCost(accessor.apply("cmc").getAsInt());
    }

    if (obj.has("colors")) {
      accessor.apply("colors").getAsJsonArray().forEach(e -> builder.addColor(
          jsonColorMappingFunction.apply(e.getAsString())));
    }

    builder.setCardType(accessor.apply("type").getAsString());

    if (obj.has("multiverseid")) {
      builder.setMultiverseId(accessor.apply("multiverseid").getAsString());
    }

    if (obj.has("supertypes")) {
      accessor.apply("supertypes").getAsJsonArray().forEach(e -> builder.addCardSuperType(
          jsonCardSuperTypeMappingFunction.apply(e.getAsString())));
    }

    ImmutableSet.Builder<CardTypeComponent> cardTypeComponentsBuilder = ImmutableSet.builder();
    accessor.apply("types").getAsJsonArray().forEach(e ->
        cardTypeComponentsBuilder.add(jsonCardTypeComponentMappingFunction.apply(e.getAsString())));
    CardType cardType = CardType.getComponentToCardTypeFunction().apply(
        cardTypeComponentsBuilder.build());
    builder.setCardClassification(cardType);

    accessor.apply("subtypes").getAsJsonArray().forEach(e -> builder.addSubType(e.getAsString()));
    builder.setRarity(rarityMapping.apply(accessor.apply("rarity").getAsString()));
    builder.setText(textWithSymbolsMapping.apply(accessor.apply("text").getAsString()));

    return builder.build();
  }
}
