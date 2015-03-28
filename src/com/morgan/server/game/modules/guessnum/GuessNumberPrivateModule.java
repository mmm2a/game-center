package com.morgan.server.game.modules.guessnum;

import com.morgan.server.game.modules.CommonPortalPrivateModule;
import com.morgan.server.game.modules.GamePortal;
import com.morgan.shared.game.modules.GameIdentifier;

/**
 * GUICE module for the random number game.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class GuessNumberPrivateModule extends CommonPortalPrivateModule {

  public GuessNumberPrivateModule(GameIdentifier gameIdentifier) {
    super(gameIdentifier);
  }

  @Override protected Class<? extends GamePortal> getGamePortalClass() {
    return GuessNumberPortal.class;
  }
}
