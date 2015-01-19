package com.morgan.client.alert;

import java.util.HashMap;
import java.util.Map;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.morgan.client.common.HasStyleNames;
import com.morgan.client.common.TimerFactory;
import com.morgan.client.common.TimerFactory.TimerHandle;

/**
 * Default implementation of an {@link AlertController}.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
class DefaultAlertController implements AlertController {

  /**
   * Interface for the view that contains everything.
   */
  @ImplementedBy(DefaultViewContainer.class)
  interface ViewContainer extends IsWidget, HasWidgets.ForIsWidget, HasStyleNames {
  }

  /**
   * View interface for all alerts that are displayed.
   */
  @ImplementedBy(DefaultView.class)
  interface View extends IsWidget, HasWidgets.ForIsWidget, HasStyleNames {
    /** See {@link FlowPanel#insert(com.google.gwt.user.client.ui.Widget, int)}. */
    void insert(IsWidget widget, int beforeIndex);

    /**
     * Gets the number of child widgets in this view.
     */
    int getWidgetCount();

    /**
     * Gets the child widget at the given index.
     */
    AlertView getChildWidget(int index);
  }

  /**
   * View interface for a single alert that is displayed.
   */
  @ImplementedBy(DefaultAlertView.class)
  interface AlertView extends IsWidget,
      HasWidgets.ForIsWidget,
      HasStyleNames,
      HasMouseOverHandlers,
      HasMouseOutHandlers,
      HasClickHandlers {
    /**
     * Sets the priority for this alert view.
     */
    void setPriority(int priority);

    /**
     * Gets the priority of the alert displayed.
     */
    int getPriority();
  }

  private final TimerFactory timerFactory;
  private final Scheduler scheduler;
  private final View view;
  private final Provider<AlertView> alertViewProvider;
  private final AlertResources resources;

  private final Map<DefaultAlert, AlertView> displayedAlerts = new HashMap<>();
  private final Multimap<AlertView, DefaultAlertHandle> displayRequests = HashMultimap.create();
  private final Map<AlertView, TimerHandle> fadeWatchers = new HashMap<>();

  @Inject DefaultAlertController(
      TimerFactory timerFactory,
      Scheduler scheduler,
      ViewContainer viewContainer,
      View view,
      Provider<AlertView> alertViewProvider,
      AlertResources resources) {
    this.timerFactory = timerFactory;
    this.scheduler = scheduler;
    this.view = view;
    this.alertViewProvider = alertViewProvider;
    this.resources = resources;

    resources.css().ensureInjected();

    viewContainer.setStyleName(resources.css().alertsContainer());
    view.setStyleName(resources.css().alerts());

    viewContainer.add(view);
    getRootPanel().add(viewContainer);
  }

  @VisibleForTesting HasWidgets.ForIsWidget getRootPanel() {
    return RootPanel.get();
  }

  private void addViewToContainer(AlertView alertView) {
    for (int i = 0; i < view.getWidgetCount(); i++) {
      AlertView otherView = view.getChildWidget(i);
      if (otherView.getPriority() <= alertView.getPriority()) {
        view.insert(alertView, i);
        return;
      }
    }

    view.add(alertView);
  }

  private void cancelAlert(AlertView alertView, Alert alert) {
    displayRequests.removeAll(alertView);
    view.remove(alertView);
    displayedAlerts.remove(alert);
    TimerHandle timerHandle = fadeWatchers.remove(alertView);
    if (timerHandle != null) {
      timerHandle.cancel();
    }
  }

  private void stopFading(final DefaultAlert alert, final AlertView alertView) {
    TimerHandle handle = fadeWatchers.remove(alertView);
    if (handle != null) {
      handle.cancel();
    }

    alertView.removeStyleName(resources.css().fade());
  }

  private void startFading(final DefaultAlert alert, final AlertView alertView) {
    // Cancel any fade watchers that are already in play
    stopFading(alert, alertView);

    final Runnable fadeRunnable = new Runnable() {
      @Override public void run() {
        cancelAlert(alertView, alert);
      }
    };

    // We need to defer adding the fade property so the browser can be ready.
    scheduler.scheduleDeferred(new ScheduledCommand() {
      @Override public void execute() {
        // Only start fading it if the alert hasn't already been cancelled.
        if (alertView.equals(displayedAlerts.get(alert))) {
          alertView.addStyleName(resources.css().fade());
          TimerHandle handle = timerFactory.schedule(
              fadeRunnable, resources.css().FADE_DURATION_MS());
          fadeWatchers.put(alertView, handle);
        }
      }
    });
  }

  private DefaultAlertHandle requestDisplayOf(DefaultAlert alert, boolean isFading) {
    AlertView alertView = displayedAlerts.get(alert);
    if (alertView == null) {
      alertView = alertViewProvider.get();
      alertView.setPriority(alert.priority);
      for (String className : alert.cssClassNames) {
        alertView.addStyleName(className);
      }
      alertView.add(alert.contents);
      addViewToContainer(alertView);
      displayedAlerts.put(alert, alertView);
    } else {
      // If it has a fade status, we're going to reset that.
      alertView.removeStyleName(resources.css().fade());
    }

    DefaultAlertHandle handle = new DefaultAlertHandle(alert, alertView);
    displayRequests.put(alertView, handle);

    if (isFading) {
      startFading(alert, alertView);
      MouseHandlerImpl handler = new MouseHandlerImpl(alertView, alert);
      alertView.addMouseOutHandler(handler);
      alertView.addMouseOverHandler(handler);
    }

    alertView.addClickHandler(new ClickHandlerImpl(alertView, alert));

    return handle;
  }

  private void requestCancel(DefaultAlertHandle handle, AlertView alertView, Alert alert) {
    if (displayRequests.remove(alertView, handle)) {
      if (!displayRequests.containsKey(alertView)) {
        // Last one was removed, so get rid of the display
        cancelAlert(alertView, alert);
      }
    }
  }

  @Override public AlertBuilder newStatusAlertBuilder(String text) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(text));
    return newStatusAlertBuilder(SafeHtmlUtils.fromString(text));
  }

  @Override public AlertBuilder newErrorAlertBuilder(String text) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(text));
    return newErrorAlertBuilder(SafeHtmlUtils.fromString(text));
  }

  @Override public AlertBuilder newStatusAlertBuilder(SafeHtml contents) {
    Preconditions.checkNotNull(contents);
    return newStatusAlertBuilder(new HTML(contents));
  }

  @Override public AlertBuilder newErrorAlertBuilder(SafeHtml contents) {
    Preconditions.checkNotNull(contents);
    return newErrorAlertBuilder(new HTML(contents));
  }

  @Override public AlertBuilder newStatusAlertBuilder(IsWidget contents) {
    return new DefaultAlertBuilder(
        Preconditions.checkNotNull(contents), resources.css().status(), 1000, true);
  }

  @Override public AlertBuilder newErrorAlertBuilder(IsWidget contents) {
    return new DefaultAlertBuilder(
        Preconditions.checkNotNull(contents), resources.css().error(), 0, false);
  }

  /**
   * Implementation of a click handler that closes an alert.
   */
  private class ClickHandlerImpl implements ClickHandler {

    private final AlertView alertView;
    private final Alert alert;

    ClickHandlerImpl(AlertView alertView, Alert alert) {
      this.alertView = Preconditions.checkNotNull(alertView);
      this.alert = Preconditions.checkNotNull(alert);
    }

    @Override public void onClick(ClickEvent event) {
      cancelAlert(alertView, alert);
    }
  }

  /**
   * Implementation of a mouse over and mouse out handler that re-starts the fading on an alert.
   */
  private class MouseHandlerImpl implements MouseOverHandler, MouseOutHandler {

    private final AlertView alertView;
    private final DefaultAlert alert;

    MouseHandlerImpl(AlertView alertView, DefaultAlert alert) {
      this.alertView = Preconditions.checkNotNull(alertView);
      this.alert = Preconditions.checkNotNull(alert);
    }

    @Override public void onMouseOver(MouseOverEvent e) {
      stopFading(alert, alertView);
    }

    @Override public void onMouseOut(MouseOutEvent event) {
      startFading(alert, alertView);
    }
  }

  /**
   * Default implementation of the {@link AlertBuilder} interface.
   */
  private class DefaultAlertBuilder implements AlertBuilder {

    private final IsWidget contents;
    private final ImmutableSet.Builder<String> cssClassNamesBuilder = ImmutableSet.builder();

    private int priority;
    private boolean isFading;

    DefaultAlertBuilder(
        IsWidget contents, String statusOrErrorCss, int priority, boolean isFading) {
      this.contents = Preconditions.checkNotNull(contents);

      Preconditions.checkArgument(!Strings.isNullOrEmpty(statusOrErrorCss));
      this.cssClassNamesBuilder.add(resources.css().alert(), statusOrErrorCss);

      this.priority = priority;
      this.isFading = isFading;
    }

    @Override public AlertBuilder setPriority(int priority) {
      this.priority = priority;
      return this;
    }

    @Override public AlertBuilder isFading(boolean isFading) {
      this.isFading = isFading;
      return this;
    }

    @Override public AlertBuilder addStyleName(String styleName) {
      this.cssClassNamesBuilder.add(styleName);
      return this;
    }

    @Override public Alert create() {
      return new DefaultAlert(contents, cssClassNamesBuilder.build(), isFading, priority);
    }
  }

  /**
   * Default implementation of an {@link Alert}.
   */
  private class DefaultAlert implements Alert {

    private final IsWidget contents;
    private final ImmutableSet<String> cssClassNames;
    private final boolean isFading;
    private final int priority;

    DefaultAlert(
        IsWidget contents, ImmutableSet<String> cssClassNames, boolean isFading, int priority) {
      this.contents = Preconditions.checkNotNull(contents);
      this.cssClassNames = Preconditions.checkNotNull(cssClassNames);
      this.isFading = isFading;
      this.priority = priority;
    }

    @Override public AlertHandle requestDisplay() {
      return requestDisplayOf(this, isFading);
    }
  }

  /**
   * Default implementation of an {@link AlertHandle}.
   *
   * @author mark@mark-morgan.net (Mark Morgan)
   */
  private class DefaultAlertHandle implements AlertHandle {

    private final Alert alert;
    private final AlertView view;

    DefaultAlertHandle(Alert alert, AlertView view) {
      this.alert = Preconditions.checkNotNull(alert);
      this.view = Preconditions.checkNotNull(view);
    }

    @Override public void cancel() {
      requestCancel(this, view, alert);
    }
  }

  /**
   * Default implementation of the {@link ViewContainer} interface.
   */
  static class DefaultViewContainer extends SimplePanel implements ViewContainer {
  }

  /**
   * Default implementation of the {@link View} interface.
   */
  static class DefaultView extends FlowPanel implements View {

    @Override public AlertView getChildWidget(int index) {
      return (AlertView) super.getWidget(index);
    }
  }

  /**
   * Default implementatino of the {@link AlertView} interface.
   */
  static class DefaultAlertView extends FocusPanel implements AlertView {

    private int priority;

    @Override public void setPriority(int priority) {
      this.priority = priority;
    }

    @Override public int getPriority() {
      return priority;
    }
  }
}
