package com.morgan.server.backend.prod;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Function;

import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.morgan.server.backend.MtgRawCardBackend;
import com.morgan.server.backend.prod.mtgdb.MtgRawCardHelper;
import com.morgan.server.mtg.raw.Card;

/**
 * Production implementation of the {@link MtgRawCardBackend}.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class ProdMtgRawCardBackend implements MtgRawCardBackend {

  private final MtgRawCardHelper helper;

  @Inject ProdMtgRawCardBackend(MtgRawCardHelper helper) {
    this.helper = helper;
  }

  @Override public void insertCards(
      Iterable<Card> cards,
      Function<String, Path> multiverseIdToFilePathFunction) {
    try {
      helper.insertCards(cards, multiverseIdToFilePathFunction);
    } catch (IOException e) {
      Throwables.propagate(e);
    }
  }
}
