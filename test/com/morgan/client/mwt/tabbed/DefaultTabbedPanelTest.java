package com.morgan.client.mwt.tabbed;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.InsertPanel;
import com.morgan.testing.FakeCssFactory;
import com.morgan.testing.MoreProviders;

/**
 * Tests for the {@link DefaultTabbedPanel} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultTabbedPanelTest {

  private static final TabbedCss CSS = FakeCssFactory.forResource(TabbedCss.class).createProxy();
  private static final TabbedResources RESOURCES = new TabbedResources() {
    @Override public TabbedCss css() {
      return CSS;
    }
  };

  private static final String TAB_NAME_1 = "tab name 1";
  private static final String TAB_NAME_2 = "tab name 2";
  private static final String TAB_NAME_3 = "tab name 3";

  private static final SafeHtml TAB_LABEL_1 = SafeHtmlUtils.fromString("Tab label 1");
  private static final SafeHtml TAB_LABEL_2 = SafeHtmlUtils.fromString("Tab label 2");
  private static final SafeHtml TAB_LABEL_3 = SafeHtmlUtils.fromString("Tab label 3");

  @Mock private DefaultTabbedPanel.View mockView;

  @Mock private DefaultTabbedPanel.TabView mockTabView1;
  @Mock private DefaultTabbedPanel.TabView mockTabView2;
  @Mock private DefaultTabbedPanel.TabView mockTabView3;

  @Mock private HasWidgets.ForIsWidget mockTabContainerAsHasWidgets;
  @Mock private InsertPanel.ForIsWidget mockTabContainerAsInsertPanel;

  private DefaultTabbedPanel panel;

  @Before public void createTestInstances() {
    panel = new DefaultTabbedPanel(
        mockView, MoreProviders.of(mockTabView1, mockTabView2, mockTabView3), RESOURCES);
  }

  @Before public void setUpCommonMockInteractions() {
    when(mockView.getTabContainerAsHasWidgets()).thenReturn(mockTabContainerAsHasWidgets);
    when(mockView.getTabContainerAsInsertPanel()).thenReturn(mockTabContainerAsInsertPanel);

    when(mockTabView1.isEnabled()).thenReturn(true);
    when(mockTabView2.isEnabled()).thenReturn(true);
    when(mockTabView3.isEnabled()).thenReturn(true);
  }

  @Test public void createTab_firstTab_activatesTab() {
    assertThat(panel.createAndAddTab(TAB_NAME_1, TAB_LABEL_1).isActive()).isTrue();
    verify(mockTabContainerAsInsertPanel).insert(mockTabView1, 0);
    verify(mockTabView1).addStyleName(CSS.active());
  }

  @Test public void createTab_secondTab_doesNotActivateSecondTab() {
    panel.createAndAddTab(TAB_NAME_1, TAB_LABEL_1);
    panel.createAndAddTab(TAB_NAME_2, TAB_LABEL_2);
    verify(mockTabContainerAsInsertPanel).insert(mockTabView2, 1);
    verify(mockTabView2, never()).addStyleName(CSS.active());
  }

  @Test public void disableActiveTab_activatesFirstNonDisabledTab() {
    Tab tab1 = panel.createAndAddTab(TAB_NAME_1, TAB_LABEL_1);
    Tab tab2 = panel.createAndAddTab(TAB_NAME_2, TAB_LABEL_2);
    Tab tab3 = panel.createAndAddTab(TAB_NAME_3, TAB_LABEL_3);

    assertThat(tab1.isActive()).isTrue();
    assertThat(tab2.isActive()).isFalse();
    assertThat(tab3.isActive()).isFalse();

    when(mockTabView1.isEnabled()).thenReturn(false);
    tab1.setEnabled(false);

    assertThat(tab1.isActive()).isFalse();
    assertThat(tab2.isActive()).isTrue();
    assertThat(tab3.isActive()).isFalse();
  }
}
