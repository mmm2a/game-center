package com.morgan.server.game;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morgan.server.game.modules.GamePortals;
import com.morgan.shared.common.BackendException;
import com.morgan.shared.game.GameService;
import com.morgan.shared.game.modules.GameDescriptor;

/**
 * Default implementation of the {@link GameService} service interface.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
class DefaultGameService extends RemoteServiceServlet implements GameService {

  static final long serialVersionUID = 1L;

  private final GamePortals gamePortals;
  private final GamePortalToGameDescriptorFunction portalToDescriptionFunction;

  @Inject DefaultGameService(GamePortals gamePortals,
      GamePortalToGameDescriptorFunction portalToDescriptionFunction) {
    this.gamePortals = gamePortals;
    this.portalToDescriptionFunction = portalToDescriptionFunction;
  }

  @Override public ImmutableSet<GameDescriptor> getAllGames() throws BackendException {
    return ImmutableSet.copyOf(
        gamePortals.getPortalsMap().values().stream()
            .map(portalToDescriptionFunction)
            .iterator());
  }
}
