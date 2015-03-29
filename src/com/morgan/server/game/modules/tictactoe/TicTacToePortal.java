package com.morgan.server.game.modules.tictactoe;

import com.google.common.util.concurrent.AbstractService;
import com.google.inject.Inject;
import com.morgan.server.game.modules.GamePortal;

/**
 * Implementation of the {@link GamePortal} interface for Tic Tac Toe.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class TicTacToePortal extends AbstractService implements GamePortal {

  @Inject TicTacToePortal() {
  }

  @Override protected void doStart() {
    // Nothing needs to be done
  }

  @Override protected void doStop() {
    // Nothing needs to be done
  }
}
