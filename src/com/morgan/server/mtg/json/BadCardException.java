package com.morgan.server.mtg.json;

import javax.annotation.Nullable;

/**
 * An exception thrown for the rare circumstance when a card is simply a bad card we're not going
 * to deal with.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class BadCardException extends RuntimeException {

  static final long serialVersionUID = 0L;

  @Nullable private String cardName;
  @Nullable private String multiverseId;

  public void setCardData(String cardName, String multiverseId) {
    this.cardName = cardName;
    this.multiverseId = multiverseId;
  }

  @Override public String getMessage() {
    return String.format("Bad card: %s[%s]", cardName, multiverseId);
  }
}
