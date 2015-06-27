package com.morgan.server.mtg.json;

import java.util.function.Function;

import org.joda.time.ReadablePartial;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.morgan.server.mtg.BorderColor;
import com.morgan.server.mtg.Card;
import com.morgan.server.mtg.CardLayout;
import com.morgan.server.mtg.CardSuperType;
import com.morgan.server.mtg.CardType;
import com.morgan.server.mtg.CardType.CardTypeComponent;
import com.morgan.server.mtg.ManaColor;
import com.morgan.server.mtg.ManaSymbol;
import com.morgan.server.mtg.Rarity;
import com.morgan.server.mtg.TextWithSymbols;
import com.morgan.server.mtg.Watermark;
import com.morgan.server.util.log.AdvancedLogger;
import com.morgan.server.util.log.InjectLogger;

/**
 * Implementation of a {@link Function} that converts a JSON representation of a
 * Magic the Gathering card into a {@link Card}.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class JsonToCardFunction implements Function<JsonObject, Card> {

  @InjectLogger private AdvancedLogger log = AdvancedLogger.NULL;

  private final JsonTypeVisitState visitState;
  private final Function<String, CardLayout> cardLayoutFunction;
  private final Function<String, ImmutableCollection<ManaSymbol>> jsonManaSymbolsFunction;
  private final Function<String, ManaColor> jsonColorMappingFunction;
  private final Function<String, CardSuperType> jsonCardSuperTypeMappingFunction;
  private final Function<String, CardTypeComponent> jsonCardTypeComponentMappingFunction;
  private final Function<String, Rarity> rarityMapping;
  private final Function<String, TextWithSymbols> textWithSymbolsMapping;
  private final Function<String, BorderColor> borderColorMapping;
  private final Function<String, Watermark> watermarkMapping;
  private final Function<String, ReadablePartial> jsonToReleaseDate;

  @Inject JsonToCardFunction(
      JsonTypeVisitState visitState,
      @JsonMapping Function<String, CardLayout> cardLayoutFunction,
      @JsonMapping Function<String, ImmutableCollection<ManaSymbol>> jsonManaSymbolsFunction,
      @JsonMapping Function<String, ManaColor> jsonColorMappingFunction,
      @JsonMapping Function<String, CardSuperType> jsonCardSuperTypeMappingFunction,
      @JsonMapping Function<String, CardTypeComponent> jsonCardTypeComponentMappingFunction,
      @JsonMapping Function<String, Rarity> rarityMapping,
      @JsonMapping Function<String, TextWithSymbols> textWithSymbolsMapping,
      @JsonMapping Function<String, BorderColor> borderColorMapping,
      @JsonMapping Function<String, Watermark> watermarkMapping,
      @JsonMapping Function<String, ReadablePartial> jsonToReleaseDate) {
    this.visitState = visitState;
    this.cardLayoutFunction = cardLayoutFunction;
    this.jsonManaSymbolsFunction = jsonManaSymbolsFunction;
    this.jsonColorMappingFunction = jsonColorMappingFunction;
    this.jsonCardSuperTypeMappingFunction = jsonCardSuperTypeMappingFunction;
    this.jsonCardTypeComponentMappingFunction = jsonCardTypeComponentMappingFunction;
    this.rarityMapping = rarityMapping;
    this.textWithSymbolsMapping = textWithSymbolsMapping;
    this.borderColorMapping = borderColorMapping;
    this.watermarkMapping = watermarkMapping;
    this.jsonToReleaseDate = jsonToReleaseDate;
  }

  private Card applyOrThrow(JsonObject obj) {
    Function<String, JsonElement> accessor = visitState.getAccessorFor("card", obj);
    Card.Builder builder = Card.builder();

    String name = accessor.apply("name").getAsString();
    builder.setName(name);

    String multiverseId = null;
    if (obj.has("multiverseid")) {
      multiverseId = accessor.apply("multiverseid").getAsString();
      builder.setMultiverseId(multiverseId);
    }

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

    if (obj.has("supertypes")) {
      accessor.apply("supertypes").getAsJsonArray().forEach(e -> builder.addCardSuperType(
          jsonCardSuperTypeMappingFunction.apply(e.getAsString())));
    }

    if (obj.has("types")) {
      ImmutableSet.Builder<CardTypeComponent> cardTypeComponentsBuilder = ImmutableSet.builder();
      accessor.apply("types").getAsJsonArray().forEach(e ->
          cardTypeComponentsBuilder.add(
              jsonCardTypeComponentMappingFunction.apply(e.getAsString())));
      CardType cardType = CardType.getComponentToCardTypeFunction().apply(
          cardTypeComponentsBuilder.build());
      builder.setCardClassification(cardType);
    }

    if (obj.has("subtypes")) {
      accessor.apply("subtypes").getAsJsonArray().forEach(e -> builder.addSubType(e.getAsString()));
    }

    builder.setRarity(rarityMapping.apply(accessor.apply("rarity").getAsString()));

    if (obj.has("text")) {
      builder.setText(textWithSymbolsMapping.apply(accessor.apply("text").getAsString()));
    } else {
      builder.setText(TextWithSymbols.builder().build());
    }

    if (obj.has("number")) {
      builder.setNumber(accessor.apply("number").getAsString());
    }

    if (obj.has("flavor")) {
      builder.setFlavor(accessor.apply("flavor").getAsString());
    }

    builder.setArtist(accessor.apply("artist").getAsString());

    if (obj.has("power")) {
      builder.setPower(accessor.apply("power").getAsString());
    }

    if (obj.has("toughness")) {
      builder.setToughness(accessor.apply("toughness").getAsString());
    }

    if (obj.has("loyalty")) {
      builder.setLoyalty(accessor.apply("loyalty").getAsInt());
    }

    if (obj.has("variations")) {
      accessor.apply("variations").getAsJsonArray().forEach(
          e -> builder.addAlternateMultiverseId(e.getAsString()));
    }

    if (obj.has("watermark")) {
      builder.setWatermark(watermarkMapping.apply(accessor.apply("watermark").getAsString()));
    }

    if (obj.has("border")) {
      builder.setBorderColorOverride(
          borderColorMapping.apply(accessor.apply("border").getAsString()));
    }

    builder.setTimeShifted(
        obj.has("timeshifted") ? accessor.apply("timeshifted").getAsBoolean() : false);

    if (obj.has("hand")) {
      builder.setHandModifier(accessor.apply("hand").getAsInt());
    }

    if (obj.has("life")) {
      builder.setLifeModifier(accessor.apply("life").getAsInt());
    }

    builder.setReserved(
        obj.has("reserved") ? accessor.apply("reserved").getAsBoolean() : false);
    builder.setStarter(
        obj.has("starter") ? accessor.apply("starter").getAsBoolean() : false);

    if (obj.has("releaseDate")) {
      builder.setReleaseDate(jsonToReleaseDate.apply(accessor.apply("releaseDate").getAsString()));
    }

    return builder.build();
  }

  @Override public Card apply(JsonObject obj) {
    try {
      return applyOrThrow(obj);
    } catch (BadCardException e) {
      e.setCardData(obj.get("name").getAsString(),
          obj.has("multiverseid") ? obj.get("multiverseid").getAsString() : "n/a");
      throw e;
    }
  }
}
