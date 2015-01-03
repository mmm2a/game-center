package com.morgan.client.constants;

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

  @Inject ClientPageConstants() {
    dictionary = Dictionary.getDictionary("gameCenterPageConstants");
  }

  public String getString(Enum<?> key) {
    return Preconditions.checkNotNull(dictionary.get(key.name()));
  }

  public int getInt(Enum<?> key) {
    return Integer.parseInt(Preconditions.checkNotNull(dictionary.get(key.name())));
  }
}
