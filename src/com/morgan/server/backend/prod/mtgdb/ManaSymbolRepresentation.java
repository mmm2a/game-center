package com.morgan.server.backend.prod.mtgdb;

import java.util.Optional;

import com.google.common.base.CharMatcher;
import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.inject.Inject;
import com.morgan.server.mtg.ManaSymbol;

/**
 * Gives a string representation of a mana symbol.
 *
 * <p>The representation is:
 * <code>
 *   &lt;version&gt;:[p|s][&lt;mana-symbol&gt;][&lt;count&gt;]
 * </code>
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class ManaSymbolRepresentation extends Converter<ManaSymbol, String> {

  private static final String VERSION = "1";
  private static final char PHYREXIAN = 'p';
  private static final char SNOW = 's';

  private final ManaColorRepresentation colorRepresentation;

  @Inject ManaSymbolRepresentation(ManaColorRepresentation colorRepresentation) {
    this.colorRepresentation = colorRepresentation;
  }

  @Override protected ManaSymbol doBackward(String b) {
    int sep = b.indexOf(':');
    Verify.verify(sep > 0, "Invalid string representation '%s'", b);
    Verify.verify(b.substring(0, sep).equals(VERSION));
    b = b.substring(sep + 1);

    ManaSymbol.Builder builder = ManaSymbol.builder();
    if (b.isEmpty()) {
      builder.clearCount();
      return builder.build();
    }

    char c = b.charAt(0);
    builder.setIsPhyrexian(c == PHYREXIAN);
    builder.setIsSnow(c == SNOW);
    if (c == PHYREXIAN || c == SNOW) {
      b = b.substring(1);
    }

    int nextDigit = CharMatcher.DIGIT.indexIn(b);
    String countString = null;
    if (nextDigit >= 0) {
      countString = b.substring(nextDigit);
      b = b.substring(0, nextDigit);
    }

    for (char cc : b.toCharArray()) {
      builder.addManaColor(colorRepresentation.reverse().convert(cc));
    }

    if (countString != null) {
      builder.setCount(Integer.parseInt(countString));
    } else {
      builder.clearCount();
    }

    return builder.build();
  }

  @Override protected String doForward(ManaSymbol a) {
    Preconditions.checkState(!a.isPhyrexian() || !a.isSnow());

    StringBuilder builder = new StringBuilder(VERSION + ":");
    if (a.isPhyrexian()) {
      builder.append(PHYREXIAN);
    }
    if (a.isSnow()) {
      builder.append(SNOW);
    }
    a.getColors().forEach(s -> builder.append(colorRepresentation.convert(s)));
    Optional<Integer> count = a.getCount();
    if (count.isPresent()) {
      builder.append(count.get().toString());
    }

    String result = builder.toString();
    ManaSymbol b = doBackward(result);
    Verify.verify(a.equals(b), "%s not equals to %s", a, b);
    return result;
  }
}
