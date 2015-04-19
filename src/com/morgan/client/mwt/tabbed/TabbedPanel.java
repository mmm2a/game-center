package com.morgan.client.mwt.tabbed;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

/**
 * Widget for displaying items in a tabbed panel.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@ImplementedBy(DefaultTabbedPanel.class)
public interface TabbedPanel extends IsWidget, HasTabs {
  /** Sets the orientation for this panel */
  void setOrientation(Orientation orientation);
}
