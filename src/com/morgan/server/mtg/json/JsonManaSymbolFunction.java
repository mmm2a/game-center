package com.morgan.server.mtg.json;

import java.util.function.Function;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.morgan.server.mtg.ManaColor;
import com.morgan.server.mtg.ManaSymbol;

/**
 * Function for converting JSON representation of mana symbols into {@link ManaSymbol} instances.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class JsonManaSymbolFunction implements Function<String, ManaSymbol> {

  private static final ImmutableSet<String> BAD_CARD_SYMBOLS = ImmutableSet.of(
      "Y", "½", "hw", "hr", "∞");
  private static final ImmutableMap<String, ManaColor> CHARS_TO_COLORS =
      ImmutableMap.<String, ManaColor>builder()
          .put("R", ManaColor.RED)
          .put("G", ManaColor.GREEN)
          .put("W", ManaColor.WHITE)
          .put("U", ManaColor.BLUE)
          .put("B", ManaColor.BLACK)
          .build();

  private static final String PHYREXIAN_CHAR = "P";
  private static final String SNOW_CHAR = "S";
  private static final String X_MANA_CHAR = "X";

  private static final CharMatcher BRACE_STRIPPER = CharMatcher.anyOf("{}");
  private static final Splitter SPLITTER = Splitter.on('/');

  @Inject JsonManaSymbolFunction() {
  }

  @Override public ManaSymbol apply(String t) {
    Preconditions.checkArgument(t.startsWith("{"));
    Preconditions.checkArgument(t.endsWith("}"));
    t = BRACE_STRIPPER.trimFrom(t);

    ManaSymbol.Builder builder = ManaSymbol.builder();

    for (String part : SPLITTER.split(t)) {
      ManaColor color = CHARS_TO_COLORS.get(part);
      if (color != null) {
        builder.addManaColor(color);
      } else if (part.equals(PHYREXIAN_CHAR)) {
        builder.setIsPhyrexian(true);
      } else if (part.equals(SNOW_CHAR)) {
        builder.setIsSnow(true);
      } else if (part.equals(X_MANA_CHAR)) {
        builder.clearCount();
      } else if (BAD_CARD_SYMBOLS.contains(part)) {
        throw new BadCardException();
      } else {
        builder.setCount(Integer.parseInt(part));
      }
    }

    return builder.build();
  }
}
