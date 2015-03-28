package com.morgan.server.game.modules.tictactoe;

import com.google.inject.PrivateModule;
import com.morgan.server.game.modules.CommonPortalPrivateModule;
import com.morgan.server.game.modules.GamePortal;
import com.morgan.shared.game.modules.GameIdentifier;

/**
 * A {@link PrivateModule} that configures the Tic Tac Toe game.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class TicTacToePrivateModule extends CommonPortalPrivateModule {

  public TicTacToePrivateModule(GameIdentifier gameIdentifier) {
    super(gameIdentifier);
  }

  @Override protected Class<? extends GamePortal> getGamePortalClass() {
    return TicTacToePortal.class;
  }
}
