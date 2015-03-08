package com.morgan.server.game.modules.testgames;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.morgan.server.game.modules.ActiveGame;
import com.morgan.server.game.modules.GameModule;

public class Game1Module implements GameModule {

  @Override public String getName() {
    return "Game 1";
  }

  @Override public SafeHtml getDescription() {
    return SafeHtmlUtils.fromString("A first example game that is used for testing purposes.");
  }

  @Override public Set<ActiveGame> getActiveGamesForCurrentUser() {
    return ImmutableSet.of();
  }
}
