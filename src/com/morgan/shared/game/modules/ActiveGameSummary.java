package com.morgan.shared.game.modules;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.morgan.shared.auth.ClientUserInformation;
import com.morgan.shared.common.HasUniqueId;

/**
 * A class representing a summary of an active game.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class ActiveGameSummary implements IsSerializable, HasUniqueId, HasGameIdentifier {

  private long activeGameId;
  private GameDescriptor hostingGame;
  private ImmutableSet<ClientUserInformation> players;
  private ImmutableSet<ClientUserInformation> turnAvailable;

  private SafeHtml gameSummary;

  private ActiveGameSummary() {
    // Default constructor for GWT
  }

  private ActiveGameSummary(
      long activeGameId,
      GameDescriptor hostingGame,
      ImmutableSet<ClientUserInformation> players,
      ImmutableSet<ClientUserInformation> turnAvailable,
      SafeHtml gameSummary) {
    this.activeGameId = activeGameId;
    this.hostingGame = Preconditions.checkNotNull(hostingGame);
    this.players = Preconditions.checkNotNull(players);
    this.turnAvailable = Preconditions.checkNotNull(turnAvailable);
    this.gameSummary = Preconditions.checkNotNull(gameSummary);
  }

  @Override public long getId() {
    return activeGameId;
  }

  @Override public GameIdentifier getGameIdentifier() {
    return hostingGame.getGameIdentifier();
  }

  public GameDescriptor getGameDescriptor() {
    return hostingGame;
  }

  public ImmutableSet<ClientUserInformation> getPlayers() {
    return players;
  }

  public ImmutableSet<ClientUserInformation> getWhoCanTakeATurn() {
    return turnAvailable;
  }

  public SafeHtml getGameSummary() {
    return gameSummary;
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
        && hostingGame.equals(other.hostingGame)
        && players.equals(other.players)
        && turnAvailable.equals(other.turnAvailable)
        && gameSummary.equals(other.gameSummary);
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(ActiveGameSummary.class)
        .add("activeGameId", activeGameId)
        .add("hostingGame", hostingGame)
        .add("players", players)
        .add("turnAvailable", turnAvailable)
        .add("gameSummary", gameSummary)
        .toString();
  }

  /**
   * Builder class for building {@link ActiveGameSummary} instances.
   *
   * @author mark@mark-morgan.net (Mark Morgan)
   */
  public static class Builder {

    private final GameDescriptor hostingGame;
    private final long activeGameId;

    private final ImmutableSet.Builder<ClientUserInformation> playersBuilder =
        ImmutableSet.builder();
    private final ImmutableSet.Builder<ClientUserInformation> turnAvailableBuilder =
        ImmutableSet.builder();

    private SafeHtml gameSummary;

    private Builder(GameDescriptor hostingGame, long activeGameId) {
      this.hostingGame = Preconditions.checkNotNull(hostingGame);
      this.activeGameId = activeGameId;
    }

    public Builder setGameSummary(SafeHtml gameSummary) {
      this.gameSummary = Preconditions.checkNotNull(gameSummary);
      return this;
    }

    public Builder addPlayer(ClientUserInformation player) {
      playersBuilder.add(player);
      return this;
    }

    public Builder addAllPlayers(Iterable<ClientUserInformation> players) {
      playersBuilder.addAll(players);
      return this;
    }

    public Builder addPlayerWithAvailableTurn(ClientUserInformation player) {
      turnAvailableBuilder.add(player);
      return this;
    }

    public Builder addAllPlayersWithAvailableTurn(Iterable<ClientUserInformation> players) {
      turnAvailableBuilder.addAll(players);
      return this;
    }

    public ActiveGameSummary build() {
      ImmutableSet<ClientUserInformation> players = playersBuilder.build();
      ImmutableSet<ClientUserInformation> turnAvailable = turnAvailableBuilder.build();

      Verify.verify(!players.isEmpty());
      Verify.verify(players.containsAll(turnAvailable));
      Verify.verifyNotNull(gameSummary);

      return new ActiveGameSummary(activeGameId, hostingGame, players, turnAvailable, gameSummary);
    }
  }
}
