package com.morgan.server.constants;

/**
 * Interface for a type that can provide page constants.
 *
 * @param <T> the enumeration type that will be used as keys into the dictionary.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface PageConstantsSource<T extends Enum<T>> {

  /**
   * Asks this provider to add its constants to the sink.
   */
  void provideConstantsInto(PageConstants constantsSink);
}
