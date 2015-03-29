package com.morgan.shared.game.modules;

import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Stores descriptive information about a game that can be displayed on the client.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class GameDescriptor implements IsSerializable, HasGameIdentifier {

  private GameIdentifier gameIdentifier;
  private String name;
  private SafeHtml description;
  private SafeUri icon;

  GameDescriptor() {
    // Default constructor for GWT
  }

  private GameDescriptor(
      GameIdentifier gameIdentifier,
      String name,
      SafeHtml description,
      SafeUri icon) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(name));

    this.gameIdentifier = Preconditions.checkNotNull(gameIdentifier);
    this.name = name;
    this.description = Preconditions.checkNotNull(description);
    this.icon = Preconditions.checkNotNull(icon);
  }

  @Override public GameIdentifier getGameIdentifier() {
    return gameIdentifier;
  }

  public String getName() {
    return name;
  }

  public SafeHtml getDescription() {
    return description;
  }

  public SafeUri getIcon() {
    return icon;
  }

  @Override public int hashCode() {
    return Objects.hash(gameIdentifier, name, description, icon);
  }

  @Override public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof GameDescriptor)) {
      return false;
    }

    GameDescriptor other = (GameDescriptor) o;
    return gameIdentifier.equals(other.gameIdentifier)
        && name.equals(other.name)
        && description.equals(other.description)
        && icon.equals(other.icon);
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(GameDescriptor.class)
        .add("gameIdentifier", gameIdentifier)
        .add("name", name)
        .add("description", description)
        .add("icon", icon)
        .toString();
  }

  public static Builder builderFor(GameIdentifier gameIdentifier) {
    return new Builder(gameIdentifier);
  }

  /**
   * Builder class for building {@link GameDescriptor} instances.
   *
   * @author mark@mark-morgan.net (Mark Morgan)
   */
  public static class Builder {

    private final GameIdentifier gameIdentifier;

    private String name;
    private SafeHtml description;
    private SafeUri icon;

    private Builder(GameIdentifier gameIdentifier) {
      this.gameIdentifier = Preconditions.checkNotNull(gameIdentifier);
    }

    public Builder setName(String name) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
      this.name = name;
      return this;
    }

    public Builder setDescription(SafeHtml description) {
      this.description = Preconditions.checkNotNull(description);
      return this;
    }

    public Builder setIcon(SafeUri icon) {
      this.icon = Preconditions.checkNotNull(icon);
      return this;
    }

    public GameDescriptor build() {
      return new GameDescriptor(gameIdentifier, name, description, icon);
    }
  }
}
