package com.morgan.server.game.modules.guessnum;

import com.google.common.util.concurrent.AbstractService;
import com.google.inject.Inject;
import com.morgan.server.game.modules.GamePortal;

/**
 * {@link GamePortal} for the random number game.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class GuessNumberPortal extends AbstractService implements GamePortal {

  @Inject GuessNumberPortal() {
  }

  @Override protected void doStart() {
    // Nothing needs to be done
  }

  @Override protected void doStop() {
    // Nothing needs to be done
  }
}
