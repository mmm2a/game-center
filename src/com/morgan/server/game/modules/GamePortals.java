package com.morgan.server.game.modules;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.morgan.shared.game.modules.GameIdentifier;

/**
 * Utility class that maintains a mapping of all game portals.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
public class GamePortals {

  private final ImmutableMap<GameIdentifier, GamePortal> gamePortals;

  @Inject GamePortals(ImmutableSet<GameIdentifier> gameIdentifiers, Injector injector) {
    ImmutableMap.Builder<GameIdentifier, GamePortal> gamePortalsBuilder = ImmutableMap.builder();
    for (GameIdentifier gameIdentifier : gameIdentifiers) {
      GamePortal portal = injector.getInstance(
          Key.get(GamePortal.class, Names.named(gameIdentifier.getIdentifier())));
      gamePortalsBuilder.put(gameIdentifier, portal);
    }
    this.gamePortals = gamePortalsBuilder.build();
  }

  public ImmutableMap<GameIdentifier, GamePortal> getPortalsMap() {
    return gamePortals;
  }

  public GamePortal getPortal(GameIdentifier gameIdentifier) {
    return Preconditions.checkNotNull(gamePortals.get(gameIdentifier));
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(GamePortals.class)
        .add("gamePortals", gamePortals)
        .toString();
  }
}
