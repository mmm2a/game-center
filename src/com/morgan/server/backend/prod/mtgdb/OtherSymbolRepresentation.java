package com.morgan.server.backend.prod.mtgdb;

import com.google.common.base.Converter;
import com.google.common.base.Verify;
import com.google.common.collect.ImmutableBiMap;
import com.google.inject.Inject;
import com.morgan.server.mtg.OtherSymbol;

/**
 * Gives a representation of an other symbol.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class OtherSymbolRepresentation extends Converter<OtherSymbol, String> {

  private static final ImmutableBiMap<OtherSymbol, String> MAP =
      ImmutableBiMap.of(
          OtherSymbol.TAP, "T",
          OtherSymbol.UNTAP, "U",
          OtherSymbol.CHAOS, "C");

  @Inject OtherSymbolRepresentation() {
  }

  @Override protected OtherSymbol doBackward(String b) {
    return Verify.verifyNotNull(MAP.inverse().get(b));
  }

  @Override protected String doForward(OtherSymbol a) {
    return Verify.verifyNotNull(MAP.get(a));
  }
}
