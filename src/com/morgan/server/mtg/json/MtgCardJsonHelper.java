package com.morgan.server.mtg.json;

import java.util.function.Function;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.morgan.server.mtg.CardSet;
import com.morgan.server.util.log.AdvancedLogger;
import com.morgan.server.util.log.InjectLogger;

/**
 * Helper class for helping turn the JSON representation of Magic the Gathering cards into
 * in memory representations.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class MtgCardJsonHelper {

  @InjectLogger private AdvancedLogger log = AdvancedLogger.NULL;

  private final JsonTypeVisitState visitState;
  private final Function<JsonObject, CardSet> jsonToCardSetFunction;

  @Inject MtgCardJsonHelper(
      JsonTypeVisitState visitState,
      @JsonMapping Function<JsonObject, CardSet> jsonToCardSetFunction) {
    this.visitState = visitState;
    this.jsonToCardSetFunction = jsonToCardSetFunction;
  }

  /**
   * Parses the input JSON object into a map of card set code to {@link CardSet} instance.
   */
  public ImmutableMap<String, CardSet> parseIntoCardSets(JsonObject cardSetsJson) {
    ImmutableMap.Builder<String, CardSet> resultBuilder = ImmutableMap.builder();

    cardSetsJson.entrySet().forEach(
        e -> resultBuilder.put(
            e.getKey(), jsonToCardSetFunction.apply(e.getValue().getAsJsonObject())));
    log.info("%s", visitState.generateReport());
    ImmutableMap<String, CardSet> cardSets = resultBuilder.build();

    return cardSets;
  }
}
