package com.morgan.server.constants;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.template.soy.data.SanitizedContent;

/**
 * Helper class for helping create the page constants for a page.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class PageConstantsHelper {

  private final ImmutableSet<PageConstantsSource> sources;
  private final Provider<PageConstants> constantsProvider;

  @Inject PageConstantsHelper(
      Set<PageConstantsSource> sources,
      Provider<PageConstants> constantsProvider) {
    this.sources = ImmutableSet.copyOf(sources);
    this.constantsProvider = constantsProvider;
  }

  /**
   * Asks this helper to create a JSON representation of all configured page constants.
   */
  public SanitizedContent createPageConstantsJson() {
    PageConstants constants = constantsProvider.get();
    for (PageConstantsSource source : sources) {
      source.provideConstantsInto(constants);
    }
    return constants.emit();
  }
}
