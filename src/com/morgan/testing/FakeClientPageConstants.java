package com.morgan.testing;

import java.util.HashMap;
import java.util.Map;

import com.morgan.client.constants.ClientPageConstants;

/**
 * Fake version of the {@link ClientPageConstants} used for testing.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class FakeClientPageConstants extends ClientPageConstants {

  private final Map<String, String> dictionary = new HashMap<>();

  public FakeClientPageConstants() {
    super(true);
  }

  @Override protected String getStringValue(String key) {
    return dictionary.get(key);
  }

  public FakeClientPageConstants setValue(Enum<?> key, String value) {
    return setValue(key.name(), value);
  }

  public FakeClientPageConstants setValue(String key, String value) {
    dictionary.put(key, value);
    return this;
  }
}
