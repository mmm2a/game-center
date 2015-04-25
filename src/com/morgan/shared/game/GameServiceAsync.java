package com.morgan.shared.game;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.morgan.shared.game.modules.GameDescriptor;

/**
 * Asynchronous version of the {@link GameService}.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface GameServiceAsync {

  /** See {@link GameService#getAllGames()} */
  void getAllGames(AsyncCallback<ImmutableSet<GameDescriptor>> callback);
}
