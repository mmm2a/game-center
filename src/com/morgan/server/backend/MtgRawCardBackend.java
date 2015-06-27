package com.morgan.server.backend;

import java.nio.file.Path;
import java.util.function.Function;

import com.morgan.server.mtg.raw.Card;

/**
 * Interface for accessing the backend MtG database with raw cards (for inserting cards using their
 * raw representation directly created from parsing input JSON files).
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface MtgRawCardBackend {

  /**
   * Inserts a card into the card database.
   */
  void insertCards(
      Iterable<Card> card,
      Function<String, Path> multiverseIdToFilePathFunction);
}
