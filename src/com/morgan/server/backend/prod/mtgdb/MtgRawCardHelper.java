package com.morgan.server.backend.prod.mtgdb;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.joda.time.ReadablePartial;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.morgan.server.mtg.CardSuperType;
import com.morgan.server.mtg.raw.Card;
import com.morgan.server.util.log.AdvancedLogger;
import com.morgan.server.util.log.InjectLogger;

/**
 * A helper class for helping with database access related to MtG and raw cards.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class MtgRawCardHelper {

  private static final Joiner MULTIVERSE_IDS_JOINER = Joiner.on(',');

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

  private static class LookupCache<T, E extends HasId> {

    private final Class<E> entityClass;
    private final EntityManager mgr;
    private final Map<T, Long> cache = new HashMap<>();

    private LookupCache(Class<E> entityClass, EntityManager mgr) {
      this.entityClass = entityClass;
      this.mgr = mgr;
    }

    public E getFor(T key, Supplier<E> entitySupplier) {

      Long id = cache.get(key);
      if (id == null) {
        E entity = entitySupplier.get();
        mgr.persist(entity);
        cache.put(key, entity.getId());
        return entity;
      } else {
        return mgr.find(entityClass, id);
      }
    }
  }

  @Transactional
  public void insertCards(
      Iterable<Card> cards,
      Function<String, Path> multiverseIdToFilePathFunction) throws IOException {

    EntityManager mgr = entityManagerProvider.get();

    LookupCache<String, CardNameEntity> nameCache = new LookupCache<>(CardNameEntity.class, mgr);
    LookupCache<CardSuperType, CardSuperTypeEntity> superTypeCache =
        new LookupCache<>(CardSuperTypeEntity.class, mgr);
    LookupCache<String, CardSubTypeEntity> subTypeCache =
        new LookupCache<>(CardSubTypeEntity.class, mgr);

    int count = 0;
    for (Card card : cards) {
      count++;
      if (count % 1000 == 0) {
        log.info("Inserting card %d into DB", count);
      } else {
        log.debug("Inserting card %d into DB", count);
      }

      ReadablePartial releaseDate = card.getReleaseDate().orElse(null);
      CardEntity entity = new CardEntity(
          card.getName(),
          card.getMultiverseId().orElse(null),
          card.getCardLayout(),
          manaSymbolsRepresentation.convert(card.getManaSymbols()),
          cardImageEntityFunction.apply(card, multiverseIdToFilePathFunction),
          card.getConvertedManaCost().orElse(null),
          card.getColors().stream()
              .map(c -> manaColorRepresentation.convert(c).toString())
              .collect(Collectors.joining()),
          card.getCardType(),
          card.getCardClassification().orElse(null),
          card.getRarity(),
          card.getText().orElse(null),
          card.getFlavorText().orElse(null),
          card.getNumber().orElse(null),
          card.getArtist(),
          card.getPower().orElse(null),
          card.getToughness().orElse(null),
          card.getLoyalty().orElse(null),
          MULTIVERSE_IDS_JOINER.join(card.getAlternateMultiverseIds()),
          card.getBorderColorOverride().orElse(null),
          card.getWatermark().orElse(null),
          card.isTimeShifted(),
          card.getHandModifier().orElse(null),
          card.getLifeModifier().orElse(null),
          card.isReserved(),
          card.isStarter(),
          (releaseDate == null) ? null : releaseDate.toString());
      mgr.persist(entity);

      for (String name : card.getAllNames()) {
        CardNameEntity nameEntity  = nameCache.getFor(name, () -> new CardNameEntity(name));
        nameEntity.addToAllCards(entity);
        entity.addToAllNames(nameEntity);
        mgr.merge(entity);
        mgr.merge(nameEntity);
      }

      for (CardSuperType type : card.getCardSuperTypes()) {
        CardSuperTypeEntity typeEntity = superTypeCache.getFor(
            type, () -> new CardSuperTypeEntity(type));
        typeEntity.addCardEntity(entity);
        entity.addToCardSuperTypes(typeEntity);
        mgr.merge(entity);
        mgr.merge(typeEntity);
      }

      for (String subType : card.getSubTypes()) {
        CardSubTypeEntity typeEntity = subTypeCache.getFor(
            subType, () -> new CardSubTypeEntity(subType));
        typeEntity.addCardEntity(entity);
        entity.addCardSubType(typeEntity);
        mgr.merge(entity);
        mgr.merge(typeEntity);
      }
    }
  }
}
