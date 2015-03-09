package com.morgan.server.game.modules;

import java.util.Objects;

import org.joda.time.ReadableInstant;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * A type class representing a game that is currently active in the system.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class ActiveGame {

  private final GameDescriptor gameDescriptor;

  private final long activeGameId;

  private final long creatingUser;
  private final ReadableInstant creationTime;
  private final ReadableInstant lastModifiedTime;
  private final ImmutableSet<Long> participatingUsers;
  private final ImmutableSet<Long> waitingOnUsers;

  private ActiveGame(
      GameDescriptor gameDescriptor,
      long activeGameId,
      long creatingUser,
      ReadableInstant creationTime,
      ReadableInstant lastModifiedTime,
      Iterable<Long> participatingUsers,
      Iterable<Long> waitingOnUsers) {
    this.gameDescriptor = Preconditions.checkNotNull(gameDescriptor);
    this.activeGameId = activeGameId;
    this.creatingUser = creatingUser;
    this.creationTime = Preconditions.checkNotNull(creationTime);
    this.lastModifiedTime = Preconditions.checkNotNull(lastModifiedTime);
    this.participatingUsers = Preconditions.checkNotNull(ImmutableSet.copyOf(participatingUsers));
    this.waitingOnUsers = Preconditions.checkNotNull(ImmutableSet.copyOf(waitingOnUsers));

    Preconditions.checkState(
        Sets.union(this.participatingUsers,
            this.waitingOnUsers).size() == this.participatingUsers.size());
  }

  public GameDescriptor getGame() {
    return gameDescriptor;
  }

  public long getActiveGameId() {
    return activeGameId;
  }

  public long getCreatingUser() {
    return creatingUser;
  }

  public ReadableInstant getCreationTime() {
    return creationTime;
  }

  public ReadableInstant getLastModifiedTime() {
    return lastModifiedTime;
  }

  public ImmutableCollection<Long> getParticipatingUsers() {
    return participatingUsers;
  }

  public ImmutableSet<Long> getWaitingOn() {
    return waitingOnUsers;
  }

  @Override public int hashCode() {
    return Objects.hash(
        gameDescriptor,
        activeGameId,
        creatingUser,
        creationTime,
        lastModifiedTime,
        participatingUsers,
        waitingOnUsers);
  }

  @Override public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof ActiveGame)) {
      return false;
    }

    ActiveGame other = (ActiveGame) o;
    return gameDescriptor.equals(other.gameDescriptor)
        && activeGameId == other.activeGameId
        && creatingUser == other.creatingUser
        && creationTime.equals(other.creationTime)
        && lastModifiedTime.equals(other.lastModifiedTime)
        && participatingUsers.equals(other.participatingUsers)
        && waitingOnUsers.equals(other.waitingOnUsers);
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(ActiveGame.class)
        .add("gameDescriptor", gameDescriptor)
        .add("activeGameId", activeGameId)
        .add("creatingUser", creatingUser)
        .add("creationTime", creationTime)
        .add("lastModifiedTime", lastModifiedTime)
        .add("participatingUsers", participatingUsers)
        .add("waitingOnUsers", waitingOnUsers)
        .toString();
  }

  /**
   * Create a new {@link Builder} instance for the given game descriptor.
   */
  public static Builder newBuilderFor(GameDescriptor gameDescriptor) {
    return new Builder(gameDescriptor);
  }

  /**
   * Builder class for the {@link ActiveGame} type.
   */
  public static class Builder {

    private final GameDescriptor gameDescriptor;
    private Long activeGameId;
    private Long creatingUser;
    private ReadableInstant creationTime;
    private ReadableInstant lastModifiedTime;
    private ImmutableList.Builder<Long> participatingUsers;
    private ImmutableList.Builder<Long> waitingOnUsers;

    private Builder(GameDescriptor descriptor) {
      this.gameDescriptor = Preconditions.checkNotNull(descriptor);
    }

    public Builder setActiveGameId(long gameId) {
      this.activeGameId = gameId;
      return this;
    }

    public Builder setCreatingUser(long creatingUser) {
      this.creatingUser = creatingUser;
      return this;
    }

    public Builder setCreationTime(ReadableInstant creationTime) {
      this.creationTime = Preconditions.checkNotNull(creationTime);
      return this;
    }

    public Builder setLastModifiedTime(ReadableInstant lastModifiedTime) {
      this.lastModifiedTime = Preconditions.checkNotNull(lastModifiedTime);
      return this;
    }

    public Builder addParticipatingUsers(long... userIds) {
      for (long userId : userIds) {
        this.participatingUsers.add(userId);
      }
      return this;
    }

    public Builder addParticipatingUsers(Iterable<Long> userIds) {
      this.participatingUsers.addAll(userIds);
      return this;
    }

    public Builder addWaitingOnUsers(long... userIds) {
      for (long userId : userIds) {
        this.waitingOnUsers.add(userId);
      }
      return this;
    }

    public Builder addWaitingOnUsers(Iterable<Long> userIds) {
      this.waitingOnUsers.addAll(userIds);
      return this;
    }

    public ActiveGame build() {
      return new ActiveGame(
          gameDescriptor,
          activeGameId,
          creatingUser,
          creationTime,
          lastModifiedTime,
          participatingUsers.build(),
          waitingOnUsers.build());
    }
  }
}
