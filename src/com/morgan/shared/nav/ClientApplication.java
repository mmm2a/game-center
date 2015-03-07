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

  /**
   * Returns the {@link ClientApplication} whose {@link #getApplicationPathToken()} matches the
   * input string.
   */
  public static ClientApplication fromPathComponent(String path) {
    for (ClientApplication app : ClientApplication.values()) {
      if (app.getApplicationPathToken().equals(path)) {
        return app;
      }
    }

    throw new IllegalArgumentException(
        "Path component " + path + " does not match any known application type");
  }
}
