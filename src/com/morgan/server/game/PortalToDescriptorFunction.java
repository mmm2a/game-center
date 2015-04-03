package com.morgan.server.game;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.inject.Inject;
import com.morgan.server.game.modules.GamePortal;
import com.morgan.shared.game.modules.GameDescriptor;

/**
 * A {@link Function} for converting a {@link GamePortal} into a {@link GameDescriptor}.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class PortalToDescriptorFunction implements Function<GamePortal, GameDescriptor> {

  private final GameStaticResources resources;

  @Inject PortalToDescriptorFunction(GameStaticResources resources) {
    this.resources = resources;
  }

  @Override @Nullable public GameDescriptor apply(@Nullable GamePortal input) {
    if (input == null) {
      return null;
    }

    GameDescriptor.Builder builder = GameDescriptor.builderFor(input.getGameIdentifier())
        .setName(input.getName())
        .setDescription(input.getDescription());
    Optional<SafeUri> icon = input.getGameIcon();
    if (icon.isPresent()) {
      builder.setIcon(icon.get());
    } else {
      builder.setIcon(resources.defaultGameIcon());
    }
    return builder.build();
  }
}
