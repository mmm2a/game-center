package com.morgan.server.mtg.json;

import java.util.Locale;
import java.util.function.Function;

import org.joda.time.LocalDate;

import com.google.common.base.Verify;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.morgan.server.mtg.BorderColor;
import com.morgan.server.mtg.Card;
import com.morgan.server.mtg.CardSet;
import com.morgan.server.mtg.SetType;
import com.morgan.server.util.log.AdvancedLogger;
import com.morgan.server.util.log.InjectLogger;

/**
 * Converts a JSON representation of a card set into a {@link CardSet}.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class JsonToCardSetFunction implements Function<JsonObject, CardSet> {

  @InjectLogger private AdvancedLogger log = AdvancedLogger.NULL;

  private final JsonTypeVisitState visitState;

  private final Function<String, BorderColor> borderColorFunction;
  private final Function<String, SetType> setTypeFunction;
  private final Function<String, Locale> languagesFunction;
  private final Function<JsonObject, Card> jsonToCardFunction;

  @Inject JsonToCardSetFunction(
      JsonTypeVisitState visitState,
      @JsonMapping Function<String, BorderColor> borderColorFunction,
      @JsonMapping Function<String, SetType> setTypeFunction,
      @JsonMapping Function<String, Locale> languagesFunction,
      @JsonMapping Function<JsonObject, Card> jsonToCardFunction) {
    this.visitState = visitState;
    this.borderColorFunction = borderColorFunction;
    this.setTypeFunction = setTypeFunction;
    this.languagesFunction = languagesFunction;
    this.jsonToCardFunction = jsonToCardFunction;
  }

  private LocalDate getDateFrom(String dateRep) {
    return new LocalDate(dateRep);
  }

  @Override public CardSet apply(JsonObject setValue) {
    Function<String, JsonElement> accessor = visitState.getAccessorFor("card-set", setValue);
    CardSet.Builder cardSetBuilder = CardSet.builderFor(
        accessor.apply("name").getAsString(),
        accessor.apply("code").getAsString());

    if (setValue.has("gathererCode")) {
      cardSetBuilder.setGathererCode(accessor.apply("gathererCode").getAsString());
    }

    if (setValue.has("oldCode")) {
      cardSetBuilder.setOldCode(accessor.apply("oldCode").getAsString());
    }

    if (setValue.has("magicCardsInfoCode")) {
      cardSetBuilder.setMagicCardsInfoCode(accessor.apply("magicCardsInfoCode").getAsString());
    }

    cardSetBuilder.setReleaseDate(getDateFrom(accessor.apply("releaseDate").getAsString()));
    cardSetBuilder.setBorderColor(
        borderColorFunction.apply(accessor.apply("border").getAsString()));
    cardSetBuilder.setSetType(setTypeFunction.apply(accessor.apply("type").getAsString()));

    if (setValue.has("block")) {
      cardSetBuilder.setBlock(accessor.apply("block").getAsString());
    }

    if (setValue.has("onlineOnly")) {
      cardSetBuilder.setOnlineOnly(accessor.apply("onlineOnly").getAsBoolean());
    }

    if (setValue.has("languagesPrinted")) {
      ImmutableSet.Builder<Locale> locales = ImmutableSet.builder();
      JsonArray array = accessor.apply("languagesPrinted").getAsJsonArray();
      array.forEach(e -> locales.add(languagesFunction.apply(e.getAsString())));
      cardSetBuilder.setLanguagesPrinted(locales.build());
    }

    JsonElement cardsElement = accessor.apply("cards");
    Verify.verify(cardsElement.isJsonArray());

    for (JsonElement e : cardsElement.getAsJsonArray()) {
      try {
        cardSetBuilder.addCard(jsonToCardFunction.apply(e.getAsJsonObject()));
      } catch (BadCardException be) {
        log.info("Encourntered a bad card: %s", be.getMessage());
      } catch (Exception ep) {
        throw ep;
      }
    }

    return cardSetBuilder.build();
  }
}
