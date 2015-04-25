package com.morgan.server.game;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.morgan.server.game.modules.GamePortal;
import com.morgan.shared.game.modules.GameDescriptor;

/**
 * {@link Function} that converts {@link GamePortal} instances into {@link GameDescriptor}
 * instances.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class GamePortalToGameDescriptorFunction implements Function<GamePortal, GameDescriptor> {

  private final GameStaticResources gameStaticResources;

  @Inject GamePortalToGameDescriptorFunction(GameStaticResources gameStaticResources) {
    this.gameStaticResources = gameStaticResources;
  }

  @Override
  @Nullable public GameDescriptor apply(@Nullable GamePortal input) {
    if (input == null) {
      return null;
    }

    return GameDescriptor.builderFor(input.getGameIdentifier())
        .setDescription(input.getDescription())
        .setName(input.getName())
        .setIcon(input.getGameIcon().or(gameStaticResources.defaultGameIcon()))
        .build();
  }
}
