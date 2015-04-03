package com.morgan.server.game.modules.guessnum;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.AbstractService;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.inject.Inject;
import com.morgan.server.game.modules.GamePortal;
import com.morgan.shared.game.modules.GameIdentifier;

/**
 * {@link GamePortal} for the random number game.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class GuessNumberPortal extends AbstractService implements GamePortal {

  private final GameIdentifier gameIdentifier;

  @Inject GuessNumberPortal(GameIdentifier gameIdentifier) {
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
    return "Guess the Number";
  }

  @Override public SafeHtml getDescription() {
    return SafeHtmlUtils.fromString("\"I'm thinking of a number between 1 and 100. Can you guess "
        + "what it is in less than 10 attempts?\"");
  }
}
