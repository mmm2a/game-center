package com.morgan.client.mwt.tabbed;

import java.util.ArrayList;
import java.util.Iterator;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.morgan.client.common.HasStyleNames;
import com.morgan.client.mwt.a11y.AccessibleFocusPanel;

/**
 * Default implementation of a {@link TabbedPanel}.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class DefaultTabbedPanel extends AbstractTabbedPanel implements TabbedPanel {

  private static final Function<DefaultTab, String> TAB_TO_NAME_FUNC =
      new Function<DefaultTab, String>() {
        @Override public String apply(DefaultTab input) {
          return input.getName();
        }
      };

  /**
   * View interface for the entire tabbed panel.
   *
   * @author mark@mark-morgan.net (Mark Morgan)
   */
  @ImplementedBy(DefaultTabbedPanelView.class)
  interface View extends IsWidget, HasStyleNames {
    /** Gets the container that is used to manage the tabs. */
    HasWidgets.ForIsWidget getTabContainerAsHasWidgets();

    /** Gets the container that is used to manage the tabs. */
    InsertPanel.ForIsWidget getTabContainerAsInsertPanel();

    /**
     * Sets the content to display for the current tab.
     */
    void setContent(IsWidget content);

    /**
     * Clears the content widget of all content.
     */
    void clearContent();
  }

  /**
   * A view interface for an individual tab.
   *
   * @author mark@mark-morgan.net (Mark Morgan)
   */
  @ImplementedBy(DefaultTabView.class)
  interface TabView
      extends IsWidget, HasStyleNames, HasSafeHtml, HasEnabled, HasSelectionHandlers<Widget> {
    /** Sets the tab index for this tab */
    void setTabIndex(int tabIndex);
  }

  private final View view;
  private final Provider<TabView> tabViewProvider;
  private final ArrayList<DefaultTab> tabs = new ArrayList<>();
  private final TabbedResources resources;

  @Nullable private DefaultTab activeTab = null;

  @Inject DefaultTabbedPanel(
      View view,
      Provider<TabView> tabViewProvider,
      TabbedResources resources) {
    this.view = view;
    this.tabViewProvider = tabViewProvider;
    this.resources = resources;

    resources.css().ensureInjected();
  }

  private void removeTab(DefaultTab tab) {
    view.getTabContainerAsHasWidgets().remove(tab.getTabView());
    tabs.remove(tab);
    if (activeTab == tab) {
      activeTab = null;
      if (!tabs.isEmpty()) {
        Iterables.getFirst(tabs, null).activate();
      }
    }
  }

  private boolean isTabActive(DefaultTab tab) {
    return activeTab == tab;
  }

  private void activateTab(DefaultTab tab) {
    Preconditions.checkNotNull(tab);

    if (tab != activeTab) {
      if (activeTab != null) {
        activeTab.getTabView().removeStyleName(resources.css().active());
      }

      view.clearContent();
      activeTab = tab;
      activeTab.getTabView().addStyleName(resources.css().active());
      view.setContent(activeTab.getContent());
    }
  }

  private boolean isTabEnabled(DefaultTab tab) {
    return tab.getTabView().isEnabled();
  }

  private void setTabEnabled(DefaultTab tab, boolean isEnabled) {
    TabView tabView = tab.getTabView();
    tabView.setEnabled(isEnabled);

    tabView.removeStyleName(resources.css().disabled());
    if (!isEnabled) {
      tabView.addStyleName(resources.css().disabled());
    }

    // If we just disabled the current tab, then we need to find a tab that is enabled and activate
    // it
    if (activeTab == tab && !isEnabled) {
      for (DefaultTab t : tabs) {
        if (t.isEnabled()) {
          t.activate();
          break;
        }
      }
    }
  }

  private void setTabContent(DefaultTab tab, @Nullable IsWidget content) {
    if (isTabActive(tab)) {
      view.clearContent();
      if (content != null) {
        view.setContent(content);
      }
    }
  }

  @Override public ImmutableSet<String> getTabNames() {
    return ImmutableSet.copyOf(Iterables.transform(tabs, TAB_TO_NAME_FUNC));
  }

  @Override public Tab getTabAtIndex(int index) throws IndexOutOfBoundsException {
    return tabs.get(index);
  }

  @Override public Tab getTabNamed(final String name) throws IllegalArgumentException {
    DefaultTab tab = Iterables.getOnlyElement(Iterables.filter(tabs,
        new Predicate<DefaultTab>() {
          @Override public boolean apply(DefaultTab input) {
            return input.getName().equals(name);
          }
        }), null);
    Preconditions.checkArgument(tab != null);
    return tab;
  }

  @Override public int size() {
    return tabs.size();
  }

  @Override public boolean isEmpty() {
    return tabs.isEmpty();
  }

  @Override public void clear() {
    view.getTabContainerAsHasWidgets().clear();
    view.clearContent();
    tabs.clear();
    activeTab = null;
  }

  @Override public Tab createAndAddTab(String tabName, SafeHtml tabLabel)
      throws IllegalArgumentException {
    return createAndInsertTab(tabName, tabLabel, size());
  }

  @Override public Tab createAndInsertTab(String tabName, SafeHtml tabLabel ,int index)
      throws IllegalArgumentException, IndexOutOfBoundsException {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(tabName));
    Preconditions.checkArgument(!getTabNames().contains(tabName));
    Preconditions.checkArgument(index >= 0);
    Preconditions.checkArgument(index <= size());

    TabView tabView = tabViewProvider.get();
    DefaultTab tab = new DefaultTab(tabName, tabLabel, tabView);
    tabView.addSelectionHandler(tab);

    tabView.setHTML(tabLabel);

    tabs.add(index, tab);
    view.getTabContainerAsInsertPanel().insert(tabView, index);

    if (activeTab == null || !activeTab.isEnabled()) {
      tab.activate();
    }

    return tab;
  }

  @Override public Iterator<Tab> iterator() {
    return ImmutableList.<Tab>copyOf(tabs).iterator();
  }

  @Override public void setOrientation(Orientation orientation) {
    view.removeStyleName(resources.css().vertical());
    if (orientation == Orientation.VERTICAL) {
      view.addStyleName(resources.css().vertical());
    }
  }

  @Override public Widget asWidget() {
    return view.asWidget();
  }

  /**
   * Default implementation of a {@link Tab} used internally in the {@link DefaultTabbedPanel}
   * implementation.
   *
   * @author mark@mark-morgan.net (Mark Morgan)
   */
  private class DefaultTab implements Tab, SelectionHandler<Widget> {

    private final String name;
    private final TabView tabView;

    private SafeHtml tabLabel;

    @Nullable private IsWidget content;

    DefaultTab(String name, SafeHtml tabLabel, TabView tabView) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(name));

      this.name = name;
      this.tabLabel = Preconditions.checkNotNull(tabLabel);
      this.tabView = Preconditions.checkNotNull(tabView);
    }

    TabView getTabView() {
      return tabView;
    }

    @Nullable IsWidget getContent() {
      return content;
    }

    @Override public boolean isEnabled() {
      return isTabEnabled(this);
    }

    @Override public void setEnabled(boolean enabled) {
      setTabEnabled(this, enabled);
    }

    @Override public void setWidget(IsWidget widget) {
      this.content = widget;
      setTabContent(this, Preconditions.checkNotNull(widget));
    }

    @Override public boolean isActive() {
      return isTabActive(this);
    }

    @Override public void activate() {
      activateTab(this);
    }

    @Override public void remove() {
      removeTab(this);
    }

    @Override public String getName() {
      return name;
    }

    @Override public void setTabLabel(SafeHtml tabLabel) {
      this.tabLabel = Preconditions.checkNotNull(tabLabel);
      tabView.setHTML(tabLabel);
    }

    @Override public SafeHtml getTabLabel() {
      return tabLabel;
    }

    @Override public TabbedPanel getParentPanel() {
      return DefaultTabbedPanel.this;
    }

    @Override public void onSelection(SelectionEvent<Widget> event) {
      activateTab(this);
    }
  }

  /**
   * Default implementation of the {@link TabView} view interface.
   *
   * @author mark@mark-morgan.net (Mark Morgan)
   */
  static class DefaultTabView extends AccessibleFocusPanel implements TabView {

    private final HTML label = new HTML();

    @Inject DefaultTabView(TabbedResources resources) {
      setWidget(label);

      resources.css().ensureInjected();
      setStyleName(resources.css().tab());
    }

    @Override public void setHTML(SafeHtml html) {
      label.setHTML(html);
    }
  }

  @Override public Tab createAndAddTab(Enum<?> tabName, SafeHtml tabLabel)
      throws IllegalArgumentException {
    return createAndAddTab(tabName.name(), tabLabel);
  }

  @Override public Tab createAndInsertTab(Enum<?> tabName, SafeHtml tabLabel, int index)
      throws IllegalArgumentException, IndexOutOfBoundsException {
    return createAndInsertTab(tabName.name(), tabLabel, index);
  }
}
