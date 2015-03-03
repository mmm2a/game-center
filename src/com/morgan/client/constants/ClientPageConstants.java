package com.morgan.client.constants;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.gwt.i18n.client.Dictionary;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Contains all page constants that were delivered from the server in the page when it was rendered.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
public class ClientPageConstants {

  private final Dictionary dictionary;

  @VisibleForTesting
  protected ClientPageConstants(boolean isForTesting) {
    Preconditions.checkArgument(isForTesting);
    dictionary = null;
  }

  @Inject ClientPageConstants() {
    dictionary = Dictionary.getDictionary("gameCenterPageConstants");
  }

  protected String getStringValue(String key) {
    return dictionary.get(key);
  }

  public final String getString(Enum<?> key) {
    return getStringValue(key.name());
  }

  public final int getInt(Enum<?> key) {
    return Integer.parseInt(getStringValue(key.name()));
  }

  public final boolean getBoolean(Enum<?> key) {
    return Boolean.parseBoolean(getStringValue(key.name()));
  }
}
