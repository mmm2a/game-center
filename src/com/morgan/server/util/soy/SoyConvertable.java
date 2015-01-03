package com.morgan.server.util.soy;

import com.google.template.soy.data.SoyData;

/**
 * An interface for a type that can convert itself into a {@link SoyData} object.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface SoyConvertable {

  /**
   * Converts this instance into a {@link SoyData} instance.
   */
  SoyData toSoyData();
}
