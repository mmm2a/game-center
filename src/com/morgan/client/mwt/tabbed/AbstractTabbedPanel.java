package com.morgan.client.mwt.tabbed;

import java.util.Collection;
import java.util.LinkedList;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * Abstract implementation of the {@link TabbedPanel} interface that handles basic functionality.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public abstract class AbstractTabbedPanel implements TabbedPanel {

  private final Collection<TabListener> listeners = new LinkedList<>();

  /**
   * Notifies all registered {@link TabListener} instances that a tab was activated.
   */
  protected void fireTabActivated(Tab tab) {
    for (TabListener listener : ImmutableList.copyOf(listeners)) {
      listener.onTabActivated(tab);
    }
  }

  /**
   * Notifies all registered {@link TabListener} instances that a tab was deactivated.
   */
  protected void fireTabDeactivated(Tab tab) {
    for (TabListener listener : ImmutableList.copyOf(listeners)) {
      listener.onTabDeactivated(tab);
    }
  }

  @Override public void addTabListener(TabListener tabListener) {
    listeners.add(Preconditions.checkNotNull(tabListener));
  }

  @Override public void removeTabListener(TabListener tabListener) {
    listeners.remove(tabListener);
  }
}
