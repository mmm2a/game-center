package com.morgan.shared.game.modules;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.morgan.shared.common.HasUniqueId;

/**
 * A class representing a summary of an active game.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class ActiveGameSummary implements IsSerializable, HasUniqueId, HasGameIdentifier {

  private long activeGameId;
  private GameIdentifier hostingGame;

  private ActiveGameSummary() {
    // Default constructor for GWT
  }

  private ActiveGameSummary(
      long activeGameId,
      GameIdentifier hostingGame) {
    this.activeGameId = activeGameId;
    this.hostingGame = Preconditions.checkNotNull(hostingGame);
  }

  @Override public long getId() {
    return activeGameId;
  }

  @Override public GameIdentifier getGameIdentifier() {
    return hostingGame;
  }

  @Override public int hashCode() {
    return Long.hashCode(activeGameId);
  }

  @Override public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof ActiveGameSummary)) {
      return false;
    }

    ActiveGameSummary other = (ActiveGameSummary) o;
    return activeGameId == other.activeGameId
        && hostingGame.equals(other.hostingGame);
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(ActiveGameSummary.class)
        .add("activeGameId", activeGameId)
        .add("hostingGame", hostingGame)
        .toString();
  }
}
