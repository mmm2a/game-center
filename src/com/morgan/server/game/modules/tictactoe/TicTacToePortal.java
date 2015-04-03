package com.morgan.server.game.modules.tictactoe;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.AbstractService;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.inject.Inject;
import com.morgan.server.game.modules.GamePortal;
import com.morgan.shared.game.modules.GameIdentifier;

/**
 * Implementation of the {@link GamePortal} interface for Tic Tac Toe.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class TicTacToePortal extends AbstractService implements GamePortal {

  private final GameIdentifier gameIdentifier;

  @Inject TicTacToePortal(GameIdentifier gameIdentifier) {
    this.gameIdentifier = gameIdentifier;
  }

  @Override protected void doStart() {
    // Nothing needs to be done
  }

  @Override protected void doStop() {
    // Nothing needs to be done
  }

  @Override public Optional<SafeUri> getGameIcon() {
    return Optional.absent();
  }

  @Override public GameIdentifier getGameIdentifier() {
    return gameIdentifier;
  }

  @Override public String getName() {
    return "Tic-Tac-Toe";
  }

  @Override public SafeHtml getDescription() {
    return SafeHtmlUtils.fromString(
        "The classic game of Xs and Os, brought to life in a digitial age!");
  }
}
