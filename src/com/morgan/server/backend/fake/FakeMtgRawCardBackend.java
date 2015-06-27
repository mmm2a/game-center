package com.morgan.server.backend.fake;

import java.nio.file.Path;
import java.util.function.Function;

import com.morgan.server.backend.MtgRawCardBackend;
import com.morgan.server.mtg.raw.Card;

/**
 * Fake version of the {@link MtgRawCardBackend}.
 */
class FakeMtgRawCardBackend implements MtgRawCardBackend {

  @Override public void insertCards(
      Iterable<Card> card,
      Function<String, Path> multiverseIdToFilePathFunction) {
    throw new UnsupportedOperationException();
  }
}
