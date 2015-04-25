package com.morgan.shared.game;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.morgan.shared.common.BackendException;
import com.morgan.shared.game.modules.GameDescriptor;

/**
 * Service interface for the game service used by the game.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RemoteServiceRelativePath("../services/game-service")
public interface GameService extends RemoteService {

  /**
   * Gets the set of all games currently supported by the backend.
   */
  ImmutableSet<GameDescriptor> getAllGames() throws BackendException;
}
