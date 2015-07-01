package com.morgan.server.backend.prod.mtgdb;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.base.Throwables;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.morgan.server.mtg.ImageType;
import com.morgan.server.mtg.raw.Card;

class CardImageEntityFunction implements BiFunction<Card, Function<String, Path>, CardImageEntity> {

  @Inject CardImageEntityFunction() {
  }

  private static byte[] readImageDataFrom(Path imagePath) throws IOException {
    try (InputStream in = new FileInputStream(imagePath.toFile())) {
      return ByteStreams.toByteArray(in);
    }
  }

  @Override
  @Nullable public CardImageEntity apply(
      Card card, Function<String, Path> multiverseIdToFilePathFunction) {

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

        try {
          imageData = readImageDataFrom(imagePath);
        } catch (Exception e) {
          Throwables.propagate(e);
        }

        cardImageEntity = new CardImageEntity(multiverseId.get(), imageType, imageData);
      }
    }

    return cardImageEntity;
  }
}
