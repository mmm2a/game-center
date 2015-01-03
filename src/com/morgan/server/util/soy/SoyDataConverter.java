package com.morgan.server.util.soy;

import javax.annotation.Nullable;

import com.google.template.soy.data.SoyData;
import com.google.template.soy.data.SoyMapData;

/**
 * Interface for a function that can convert a java object into a {@link SoyData} instance.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface SoyDataConverter {
  /**
   * Asks this convert to add the given value object to the {@link SoyMapData} with the indicated
   * name.
   */
  void addToSoyMapData(SoyMapData map, String name, @Nullable Object value);
}
