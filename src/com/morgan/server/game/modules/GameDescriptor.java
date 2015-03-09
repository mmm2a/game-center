package com.morgan.server.game.modules;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.morgan.shared.game.modules.GameIdentifier;

/**
 * A description class representing a game that is registered with the system.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public abstract class GameDescriptor {

  private final GameIdentifier gameId;
  private final Optional<SafeUri> icon;
  private final String name;
  private final SafeHtml description;

  public GameDescriptor(
      GameIdentifier gameId,
      Optional<SafeUri> icon,
      String name,
      SafeHtml description) {
    this.gameId = Preconditions.checkNotNull(gameId);
    this.icon = Preconditions.checkNotNull(icon);
    this.name = name;
    this.description = Preconditions.checkNotNull(description);

    Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
    Preconditions.checkArgument(!description.equals(SafeHtmlUtils.EMPTY_SAFE_HTML));
  }

  protected ToStringHelper addToStringFields(ToStringHelper helper) {
    return helper.add("gameId", gameId)
        .add("icon", icon)
        .add("name", name)
        .add("description", description);
  }

  public GameIdentifier getGameId() {
    return gameId;
  }

  public Optional<SafeUri> getIcon() {
    return icon;
  }

  public String getName() {
    return name;
  }

  public SafeHtml getDescription() {
    return description;
  }

  /**
   * Gets the list of {@link ActiveGame} instances representing games from this game module that the
   * current user is involved in.
   */
  public abstract ImmutableSet<ActiveGame> getActiveGamesForCurrentUser();

  @Override public int hashCode() {
    return gameId.hashCode();
  }

  @Override public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof GameDescriptor)) {
      return false;
    }

    GameDescriptor other = (GameDescriptor) o;
    return gameId.equals(other.gameId)
        && icon.equals(other.icon)
        && name.equals(other.name)
        && description.equals(other.description);
  }

  @Override public final String toString() {
    return addToStringFields(MoreObjects.toStringHelper(getClass())).toString();
  }
}
