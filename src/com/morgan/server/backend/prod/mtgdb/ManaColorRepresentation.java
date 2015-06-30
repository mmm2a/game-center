package com.morgan.server.backend.prod.mtgdb;

import com.google.common.base.Converter;
import com.google.common.base.Verify;
import com.google.common.collect.ImmutableBiMap;
import com.google.inject.Inject;
import com.morgan.server.mtg.ManaColor;

/**
 * A text representation of mana color that can be used to store values in the database.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class ManaColorRepresentation extends Converter<ManaColor, Character> {

  private static final ImmutableBiMap<ManaColor, Character> colorBiMap;

  static {
    colorBiMap = ImmutableBiMap.<ManaColor, Character>builder()
        .put(ManaColor.WHITE, 'W')
        .put(ManaColor.BLUE, 'U')
        .put(ManaColor.GREEN, 'G')
        .put(ManaColor.BLACK, 'B')
        .put(ManaColor.RED, 'R')
        .build();

    for (ManaColor color : ManaColor.values()) {
      Verify.verify(colorBiMap.containsKey(color));
    }
  }

  @Inject ManaColorRepresentation() {
  }

  @Override protected ManaColor doBackward(Character b) {
    return Verify.verifyNotNull(colorBiMap.inverse().get(b));
  }

  @Override protected Character doForward(ManaColor a) {
    return Verify.verifyNotNull(colorBiMap.get(a));
  }
}
