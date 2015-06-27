package com.morgan.server.mtg.json;

import java.util.Locale;
import java.util.function.Function;

import org.joda.time.LocalDate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.morgan.server.mtg.TextWithSymbols;
import com.morgan.server.mtg.raw.CardExtraInformation;
import com.morgan.server.util.log.AdvancedLogger;
import com.morgan.server.util.log.InjectLogger;

/**
 * Implementation of a {@link Function} that converts a JSON representaiton of card extra
 * information into a {@link CardExtraInformation}.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class JsonToCardExtraInformationFunction implements Function<JsonObject, CardExtraInformation> {

  @InjectLogger private AdvancedLogger log = AdvancedLogger.NULL;

  private final JsonTypeVisitState visitState;

  private final Function<String, TextWithSymbols> textWithSymbolsFunction;
  private final Function<String, Locale> languageToLocaleFunction;

  @Inject JsonToCardExtraInformationFunction(
      JsonTypeVisitState visitState,
      @JsonMapping Function<String, TextWithSymbols> textWithSymbolsFunction,
      @JsonMapping Function<String, Locale> languageToLocaleFunction) {
    this.visitState = visitState;
    this.textWithSymbolsFunction = textWithSymbolsFunction;
    this.languageToLocaleFunction = languageToLocaleFunction;
  }

  private void addRulings(
      JsonObject obj,
      Function<String, JsonElement> accessor,
      CardExtraInformation.Builder builder) {
    if (obj.has("rulings")) {
      JsonArray array = accessor.apply("rulings").getAsJsonArray();
      array.forEach(e -> {
        JsonObject ruling = e.getAsJsonObject();
        String dateString = ruling.get("date").getAsString();
        String textString = ruling.get("text").getAsString();
        textString = textString.replaceAll("\\Q{PW}\\E", "{W/P}");
        textString = textString.replaceAll("\\Q{(b/r)}\\E", "{B/R}");
        try {
          builder.addRuling(new LocalDate(dateString), textWithSymbolsFunction.apply(textString));
        } catch (Exception ep) {
          System.err.format("Couldn't parse '%s'\n", textString);
        }
      });
    }
  }

  private void addForeignNames(
      JsonObject obj,
      Function<String, JsonElement> accessor,
      CardExtraInformation.Builder builder) {
    if (obj.has("foreignNames")) {
      JsonArray array = accessor.apply("foreignNames").getAsJsonArray();
      array.forEach(e -> {
        JsonObject nameObj = e.getAsJsonObject();
        String languageString = nameObj.get("language").getAsString();
        String foreignName = nameObj.get("name").getAsString();
        Locale locale = languageToLocaleFunction.apply(languageString);
        builder.addForeignName(locale, foreignName);
      });
    }
  }

  @Override public CardExtraInformation apply(JsonObject obj) {
    Function<String, JsonElement> accessor = visitState.getAccessorFor("card", obj);
    CardExtraInformation.Builder builder = CardExtraInformation.builder();

    addRulings(obj, accessor, builder);
    addForeignNames(obj, accessor, builder);

    return builder.build();
  }
}
