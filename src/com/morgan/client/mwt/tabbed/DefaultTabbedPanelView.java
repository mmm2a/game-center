package com.morgan.client.mwt.tabbed;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets.ForIsWidget;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * Default implementation of the {@link DefaultTabbedPanel.View} view interface.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class DefaultTabbedPanelView extends Composite implements DefaultTabbedPanel.View {

  @UiTemplate("resources/TabbedPanel.ui.xml")
  interface Binder extends UiBinder<Widget, DefaultTabbedPanelView> {
  }

  private static final UiBinder<Widget, DefaultTabbedPanelView> binder =
      GWT.create(Binder.class);

  @UiField FlowPanel tabsContainer;
  @UiField SimplePanel contentContainer;

  @Inject DefaultTabbedPanelView() {
    initWidget(binder.createAndBindUi(this));
  }

  @Override public ForIsWidget getTabContainerAsHasWidgets() {
    return tabsContainer;
  }

  @Override public InsertPanel.ForIsWidget getTabContainerAsInsertPanel() {
    return tabsContainer;
  }

  @Override public void setContent(IsWidget content) {
    contentContainer.setWidget(content);
  }

  @Override public void clearContent() {
    contentContainer.clear();
  }
}
