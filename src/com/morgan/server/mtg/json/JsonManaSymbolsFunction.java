package com.morgan.server.mtg.json;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.morgan.server.mtg.ManaSymbol;

/**
 * {@link Function} for converting a {@link String} representing a series of mana symbols into a
 * collection of {@link ManaSymbol} instances.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class JsonManaSymbolsFunction implements Function<String, ImmutableCollection<ManaSymbol>> {

  private static final Pattern SYMBOL_PATTERN = Pattern.compile("\\{[^\\}]+\\}");

  private final Function<String, ManaSymbol> jsonManaSymbolFunction;

  @Inject JsonManaSymbolsFunction(
      @JsonMapping Function<String, ManaSymbol> jsonManaSymbolFunction) {
    this.jsonManaSymbolFunction = jsonManaSymbolFunction;
  }

  @Override public ImmutableCollection<ManaSymbol> apply(String t) {
    ImmutableCollection.Builder<ManaSymbol> symbolsBuilder = ImmutableList.builder();

    Matcher matcher = SYMBOL_PATTERN.matcher(t);
    while (matcher.find()) {
      symbolsBuilder.add(jsonManaSymbolFunction.apply(matcher.group(0)));
    }

    return symbolsBuilder.build();
  }
}
