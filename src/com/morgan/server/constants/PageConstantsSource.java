package com.morgan.server.constants;

/**
 * Interface for a type that can provide page constants.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface PageConstantsSource {

  /**
   * Asks this provider to add its constants to the sink.
   */
  void provideConstantsInto(PageConstants constantsSink);
}
