package com.morgan.server.game.modules;

import com.google.common.base.Preconditions;
import com.google.inject.PrivateModule;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.morgan.shared.game.modules.GameIdentifier;

/**
 * Common {@link PrivateModule} base class for most games.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public abstract class CommonPortalPrivateModule extends PrivateModule {

  private final GameIdentifier gameIdentifier;

  protected CommonPortalPrivateModule(GameIdentifier gameIdentifier) {
    this.gameIdentifier = Preconditions.checkNotNull(gameIdentifier);
  }

  /**
   * Gives sub-classed modules an opportunity to additionally configure items.
   */
  protected void additionalConfigure() {
  }

  /**
   * Gets the {@link Class} instance of the {@link GamePortal} class for this module.
   */
  protected abstract Class<? extends GamePortal> getGamePortalClass();

  @Override protected final void configure() {
    bind(GameIdentifier.class).toInstance(gameIdentifier);

    Named named = Names.named(gameIdentifier.getIdentifier());
    bind(GamePortal.class)
        .annotatedWith(named)
        .to(getGamePortalClass());
    expose(GamePortal.class).annotatedWith(named);
  }
}
