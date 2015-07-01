package com.morgan.server.backend.prod.mtgdb;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.morgan.server.mtg.raw.Card;
import com.morgan.server.util.log.AdvancedLogger;
import com.morgan.server.util.log.InjectLogger;

/**
 * A helper class for helping with database access related to MtG and raw cards.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class MtgRawCardHelper {

  @InjectLogger private AdvancedLogger log = AdvancedLogger.NULL;

  private final Provider<EntityManager> entityManagerProvider;
  private final ManaColorRepresentation manaColorRepresentation;
  private final ManaSymbolsRepresentation manaSymbolsRepresentation;
  private final CardImageEntityFunction cardImageEntityFunction;

  @Inject MtgRawCardHelper(
      Provider<EntityManager> entityManagerProvider,
      ManaColorRepresentation manaColorRepresentation,
      ManaSymbolsRepresentation manaSymbolsRepresentation,
      CardImageEntityFunction cardImageEntityFunction) {
    this.entityManagerProvider = entityManagerProvider;
    this.manaColorRepresentation = manaColorRepresentation;
    this.manaSymbolsRepresentation = manaSymbolsRepresentation;
    this.cardImageEntityFunction = cardImageEntityFunction;
  }

  @Transactional
  public void insertCards(
      Iterable<Card> cards,
      Function<String, Path> multiverseIdToFilePathFunction) throws IOException {
    EntityManager mgr = entityManagerProvider.get();

    int count = 0;
    for (Card card : cards) {
      count++;
      if (count % 1000 == 0) {
        log.info("Inserting card %d into DB", count);
      } else {
        log.debug("Inserting card %d into DB", count);
      }

      CardEntity entity = new CardEntity(
          card.getName(),
          card.getMultiverseId().orElse(null),
          card.getCardLayout(),
          manaSymbolsRepresentation.convert(card.getManaSymbols()),
          cardImageEntityFunction.apply(card, multiverseIdToFilePathFunction),
          ImmutableSet.copyOf(card.getAllNames().stream()
              .map(s -> new CardNameEntity(s))
              .iterator()),
          card.getConvertedManaCost().orElse(null),
          card.getColors().stream()
              .map(c -> manaColorRepresentation.convert(c).toString())
              .collect(Collectors.joining()),
          card.getCardType());
      mgr.persist(entity);
    }
  }
}
