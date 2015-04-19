package com.morgan.client.mwt.tabbed;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasEnabled;

/**
 * Describes a specific tab in a {@link TabbedPanel}.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface Tab extends HasEnabled, AcceptsOneWidget {

  /**
   * Gets the parent panel that owns this tab.
   */
  TabbedPanel getParentPanel();

  /** Gets the name of this tab. */
  String getName();

  /** Sets the tab label to display for this tab. */
  void setTabLabel(SafeHtml tabLabel);

  /** Gets the label that will be displayed for this tab. */
  SafeHtml getTabLabel();

  /** Indicates whether this tab is currently active or not. */
  boolean isActive();

  /** Activates (makes active) this tab. */
  void activate();

  /** Removes this tab from its parent tabbed panel. Once removed it can't be added back */
  void remove();
}
