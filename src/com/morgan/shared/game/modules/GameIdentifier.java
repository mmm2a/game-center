package com.morgan.shared.game.modules;

import com.google.common.base.CharMatcher;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * An identifier class for representing the unique ID of a game.
 *
 * <p>Valid characters include:
 * <ul>
 *   <li>lower case letters ['a', 'z']
 *   <li>hyphens
 *   <li>digits
 * </ul>
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class GameIdentifier implements IsSerializable {

  private static final int MAX_LENGTH = 32;

  private static final CharMatcher VALID_CHARS = CharMatcher.inRange('a', 'z')
      .or(CharMatcher.DIGIT)
      .or(CharMatcher.is('-'));

  private String identifier;

  GameIdentifier() {
    // Default constructor for GWT
  }

  public GameIdentifier(String identifier) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(identifier));
    Preconditions.checkArgument(VALID_CHARS.matchesAllOf(identifier));
    Preconditions.checkArgument(identifier.length() <= MAX_LENGTH);

    this.identifier = identifier;
  }

  public String getIdentifier() {
    return identifier;
  }

  @Override public int hashCode() {
    return identifier.hashCode();
  }

  @Override public boolean equals(Object o) {
    return (o instanceof GameIdentifier) && ((GameIdentifier) o).identifier.equals(identifier);
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(GameIdentifier.class)
        .add("identifier", identifier)
        .toString();
  }
}
