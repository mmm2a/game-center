package com.morgan.server.backend.prod.mtgdb;

import com.google.common.base.Converter;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.morgan.server.mtg.ManaSymbol;
import com.morgan.server.util.guavato8.BuildersAsConsumers;
import com.morgan.server.util.guavato8.BuildersAsConsumers.BuilderConsumer;

/**
 * Converts a collection of mana symbols into a representation that can be stored easily in the
 * database (and back).
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class ManaSymbolsRepresentation extends Converter<Iterable<ManaSymbol>, String> {

  private static final Splitter SPLITTER = Splitter.on('|').omitEmptyStrings().trimResults();
  private static final Joiner JOINER = Joiner.on('|');

  private final ManaSymbolRepresentation singleRep;

  @Inject ManaSymbolsRepresentation(ManaSymbolRepresentation singleRep) {
    this.singleRep = singleRep;
  }

  @Override protected Iterable<ManaSymbol> doBackward(String b) {
    BuilderConsumer<ManaSymbol, ImmutableCollection<ManaSymbol>> builder =
        BuildersAsConsumers.immutableCollectionBuilder();
    SPLITTER.splitToList(b).stream()
        .map(s -> singleRep.reverse().convert(s))
        .forEach(builder);
    return builder.build();
  }

  @Override protected String doForward(Iterable<ManaSymbol> a) {
    ImmutableList<ManaSymbol> list = ImmutableList.copyOf(a);
    return JOINER.join(list.stream()
        .map(s -> singleRep.convert(s))
        .iterator());
  }
}
