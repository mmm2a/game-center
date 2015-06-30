package com.morgan.server.backend.prod.mtgdb;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

import javax.persistence.EntityManager;

import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.morgan.server.mtg.ImageType;
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
  private final ManaSymbolsRepresentation manaSymbolsRepresentation;

  @Inject MtgRawCardHelper(
      Provider<EntityManager> entityManagerProvider,
      ManaSymbolsRepresentation manaSymbolsRepresentation) {
    this.entityManagerProvider = entityManagerProvider;
    this.manaSymbolsRepresentation = manaSymbolsRepresentation;
  }

  private static byte[] readImageDataFrom(Path imagePath) throws IOException {
    try (InputStream in = new FileInputStream(imagePath.toFile())) {
      return ByteStreams.toByteArray(in);
    }
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

      CardImageEntity cardImageEntity = null;
      byte[] imageData = null;
      Optional<String> multiverseId = card.getMultiverseId();
      if (multiverseId.isPresent()) {
        Path imagePath = multiverseIdToFilePathFunction.apply(multiverseId.get());
        if (imagePath != null) {
          ImageType imageType;
          if (imagePath.toString().endsWith(".png")) {
            imageType = ImageType.PNG;
          } else if (imagePath.toString().endsWith(".jpg")) {
            imageType = ImageType.JPG;
          } else {
            throw new IllegalArgumentException("Didn't expect image type for " + imagePath);
          }

          imageData = readImageDataFrom(imagePath);
          cardImageEntity = new CardImageEntity(multiverseId.get(), imageType, imageData);
        }
      }

      CardEntity entity = new CardEntity(manaSymbolsRepresentation, card, cardImageEntity);
      mgr.persist(entity);
    }
  }
}
