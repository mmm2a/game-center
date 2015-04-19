package com.morgan.client.mwt.tabbed;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * Interface describing a type that can have tabs added to it.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface HasTabs extends HasTabListeners, Iterable<Tab> {

  /**
   * Gets a set representing all tabs named and registered in this widget.
   */
  ImmutableSet<String> getTabNames();

  /**
   * Gets the tab at a specified index.
   */
  Tab getTabAtIndex(int index) throws IndexOutOfBoundsException;

  /**
   * Gets the tab with the given name.  If such a tab doesn't exist, throws an
   * {@link IllegalArgumentException}.
   */
  Tab getTabNamed(String name) throws IllegalArgumentException;

  /**
   * Returns the number of tabs registered with this widget.
   */
  int size();

  /**
   * Indicates whether or not this widget is empty.
   */
  boolean isEmpty();

  /**
   * Clears all registered tabs.
   */
  void clear();

  /**
   * Creates a new {@link Tab} instance and appends it to the end of the tabs registered with
   * this panel.  The name give MUST be unique within the set of registered tabs.
   *
   * @throws IllegalArgumentException if the name given is empty or not unique.
   */
  Tab createAndAddTab(String tabName, SafeHtml tabLabel) throws IllegalArgumentException;

  /**
   * Creates a new {@link Tab} instance and inserts it at the index given registered with
   * this panel.  The name give MUST be unique within the set of registered tabs.
   *
   * @throws IllegalArgumentException if the name given is empty or not unique.
   * @throws IndexOutOfBoundsException if the index is greater than the current size.
   */
  Tab createAndInsertTab(String tabName, SafeHtml tabLabel, int index)
      throws IllegalArgumentException, IndexOutOfBoundsException;
}
