package com.morgan.shared.game;

import java.util.Map;

import com.morgan.shared.nav.ApplicationPlace;
import com.morgan.shared.nav.ClientApplication;

/**
 * Abstract base class for all {@link ApplicationPlace} types that are in the game app.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public abstract class BaseGameApplicationPlace extends ApplicationPlace {

  protected BaseGameApplicationPlace(Map<String, String> parameters) {
    super(ClientApplication.GAME_SERVER, parameters);
  }

  protected BaseGameApplicationPlace() {
    super(ClientApplication.GAME_SERVER);
  }
}
