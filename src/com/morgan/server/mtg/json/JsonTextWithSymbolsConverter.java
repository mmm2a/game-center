package com.morgan.server.mtg.json;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.inject.Inject;
import com.morgan.server.mtg.ManaSymbol;
import com.morgan.server.mtg.OtherSymbol;
import com.morgan.server.mtg.TextWithSymbols;

/**
 * {@link Function} for converting a JSON text representation of text with symbols into a
 * {@link TextWithSymbols} instance.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class JsonTextWithSymbolsConverter implements Function<String, TextWithSymbols> {

  private static final Pattern PATTERN = Pattern.compile("([^\\{]+)|(\\{[^\\}]+\\})");

  private final Function<String, ManaSymbol> manaSymbolFunction;
  private final Function<String, OtherSymbol> otherSymbolFunction;

  @Inject JsonTextWithSymbolsConverter(
      @JsonMapping Function<String, ManaSymbol> manaSymbolFunction,
      @JsonMapping Function<String, OtherSymbol> otherSymbolFunction) {
    this.manaSymbolFunction = manaSymbolFunction;
    this.otherSymbolFunction = otherSymbolFunction;
  }

  @Override public TextWithSymbols apply(String t) {
    TextWithSymbols.Builder builder = TextWithSymbols.builder();

    Matcher matcher = PATTERN.matcher(t);
    while (matcher.find()) {
      String value = matcher.group(1);
      if (value != null) {
        builder.addTextNode(value);
      } else {
        value = matcher.group(2);
        OtherSymbol oSymbol = otherSymbolFunction.apply(value);
        if (oSymbol == null) {
          ManaSymbol mSymbol = manaSymbolFunction.apply(value);
          builder.addManaSymbolNode(mSymbol);
        } else {
          builder.addOtherSymbolNode(oSymbol);
        }
      }
    }

    return builder.build();
  }
}
