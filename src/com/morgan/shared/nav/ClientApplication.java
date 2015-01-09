package com.morgan.shared.nav;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * An enumeration that identifies which GWT client application something belongs to.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public enum ClientApplication {

  AUTHENTICATION("auth"),
  GAME_SERVER("game");

  private final String applicationPathToken;

  private ClientApplication(String applicationPathToken) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(applicationPathToken));

    this.applicationPathToken = applicationPathToken;
  }

  public String getApplicationPathToken() {
    return applicationPathToken;
  }
}
