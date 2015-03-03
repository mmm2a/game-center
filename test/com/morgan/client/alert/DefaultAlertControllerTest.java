package com.morgan.client.alert;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HasWidgets.ForIsWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.morgan.client.alert.DefaultAlertController.AlertView;
import com.morgan.client.alert.DefaultAlertController.View;
import com.morgan.client.alert.DefaultAlertController.ViewContainer;
import com.morgan.client.common.TimerFactory;
import com.morgan.client.common.TimerFactory.TimerHandle;
import com.morgan.testing.FakeCssFactory;
import com.morgan.testing.MoreProviders;

/**
 * Tests for the {@link DefaultAlertController} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultAlertControllerTest {

  private static final int FADE_DURATION_MS = 5000;
  private static final AlertCss CSS = FakeCssFactory.forResource(AlertCss.class)
      .whenMethod("FADE_DURATION_MS").thenReturn(FADE_DURATION_MS)
      .createProxy();
  private static final AlertResources RESOURCES = new AlertResources() {
    @Override public AlertCss css() {
      return CSS;
    }
  };

  @Mock private HasWidgets.ForIsWidget mockRootPanel;

  @Mock private TimerFactory mockTimerFactory;
  @Mock private Scheduler mockScheduler;
  @Mock private ViewContainer mockViewContainer;
  @Mock private View mockView;

  @Mock private AlertView mockAlertView1;
  @Mock private AlertView mockAlertView2;
  @Mock private AlertView mockAlertView3;

  @Mock private IsWidget mockAlertViewContents1;
  @Mock private IsWidget mockAlertViewContents2;
  @Mock private IsWidget mockAlertViewContents3;

  @Mock private TimerHandle mockTimerHandle;

  @Mock private AsyncCallback<Void> mockCallback;

  @Captor private ArgumentCaptor<ClickHandler> clickHandlerCaptor;
  @Captor private ArgumentCaptor<MouseOutHandler> mouseOutHandlerCaptor;
  @Captor private ArgumentCaptor<MouseOverHandler> mouseOverHandlerCaptor;
  @Captor private ArgumentCaptor<Runnable> runnableCaptor;

  @Captor private ArgumentCaptor<ScheduledCommand> scheduledCommandCaptor;

  private TestableDefaultAlertController controller;

  @Before public void createTestInstances() {
    controller = new TestableDefaultAlertController();
  }

  @Before public void setUpCommonMockInteractions() {
    when(mockTimerFactory.schedule(any(Runnable.class), anyInt()))
        .thenReturn(mockTimerHandle);
  }

  @Test public void construction_setsUpContainers() {
    verify(mockViewContainer).setStyleName(CSS.alertsContainer());
    verify(mockView).setStyleName(CSS.alerts());

    verify(mockViewContainer).add(mockView);
    verify(mockRootPanel).add(mockViewContainer);
  }

  @Test public void statusAlert_displays() {
    controller.newStatusAlertBuilder(mockAlertViewContents1).create().requestDisplay();
    verify(mockAlertView1).add(mockAlertViewContents1);
    verify(mockAlertView1).addStyleName(CSS.alert());
    verify(mockAlertView1).addStyleName(CSS.status());
    verify(mockView).add(mockAlertView1);
  }

  @Test public void errorAlert_displays() {
    controller.newErrorAlertBuilder(mockAlertViewContents1).create().requestDisplay();
    verify(mockAlertView1).add(mockAlertViewContents1);
    verify(mockAlertView1).addStyleName(CSS.alert());
    verify(mockAlertView1).addStyleName(CSS.error());
    verify(mockView).add(mockAlertView1);
  }

  @Test public void alert_cancelClosesAlert() {
    controller.newErrorAlertBuilder(mockAlertViewContents1).create().requestDisplay().cancel();
    verify(mockView).remove(mockAlertView1);
  }

  @Test public void alert_delegateTo_cancelsAlertOnSuccessAndCallsToDelegate() {
    controller.newErrorAlertBuilder(mockAlertViewContents1)
        .create()
        .requestDisplay()
        .delegateTo(mockCallback)
        .onSuccess(null);
    verify(mockView).remove(mockAlertView1);
    verify(mockCallback).onSuccess(null);
  }

  @Test public void alert_delegateTo_cancelsAlertOnFailureAndCallsToDelegate() {
    Throwable caught = new NullPointerException();
    controller.newErrorAlertBuilder(mockAlertViewContents1)
        .create()
        .requestDisplay()
        .delegateTo(mockCallback)
        .onFailure(caught);
    verify(mockView).remove(mockAlertView1);
    verify(mockCallback).onFailure(caught);
  }

  @Test public void alert_transfersPriorityToView() {
    controller.newStatusAlertBuilder(mockAlertViewContents1)
        .setPriority(7)
        .create()
        .requestDisplay();
    verify(mockAlertView1).setPriority(7);
  }

  @Test public void multipleAlerts_displayedInOrder() {
    when(mockAlertView1.getPriority()).thenReturn(5);
    when(mockAlertView2.getPriority()).thenReturn(10);


    controller.newStatusAlertBuilder(mockAlertViewContents1).create().requestDisplay();

    when(mockView.getWidgetCount()).thenReturn(1);
    when(mockView.getChildWidget(0)).thenReturn(mockAlertView1);

    controller.newStatusAlertBuilder(mockAlertViewContents2).create().requestDisplay();

    when(mockView.getWidgetCount()).thenReturn(2);
    when(mockView.getChildWidget(0)).thenReturn(mockAlertView2);
    when(mockView.getChildWidget(1)).thenReturn(mockAlertView1);

    controller.newStatusAlertBuilder(mockAlertViewContents3).create().requestDisplay();

    verify(mockView).add(mockAlertView1);
    verify(mockView).insert(mockAlertView2, 0);
    verify(mockView).add(mockAlertView3);
  }

  @Test public void mutlipleRequestorsForSameAlert() {
    Alert alert = controller.newStatusAlertBuilder(mockAlertViewContents1).create();

    AlertHandle handle1 = alert.requestDisplay();
    reset(mockView);
    AlertHandle handle2 = alert.requestDisplay();
    verifyZeroInteractions(mockView);

    handle1.cancel();
    verifyZeroInteractions(mockView);

    handle2.cancel();
    verify(mockView).remove(mockAlertView1);

    // Now, just show that its OK to request display again
    alert.requestDisplay();
    verify(mockView).add(mockAlertView2);
  }

  @Test public void alert_clickCloses() {
    Alert alert = controller.newStatusAlertBuilder(mockAlertViewContents1).create();
    alert.requestDisplay();

    verify(mockAlertView1).addClickHandler(clickHandlerCaptor.capture());
    clickHandlerCaptor.getValue().onClick(null);
    verify(mockView).remove(mockAlertView1);
  }

  @Test public void fadingAlert_startsFading() {
    Alert alert = controller.newStatusAlertBuilder(mockAlertViewContents1).create();
    alert.requestDisplay();

    verify(mockAlertView1, never()).addStyleName(CSS.fade());
    verify(mockScheduler).scheduleDeferred(scheduledCommandCaptor.capture());
    scheduledCommandCaptor.getValue().execute();
    verify(mockAlertView1).addStyleName(CSS.fade());
  }

  @Test public void fadingAlert_closesAutomaticallyAfterFade() {
    Alert alert = controller.newStatusAlertBuilder(mockAlertViewContents1).create();
    alert.requestDisplay();

    verify(mockScheduler).scheduleDeferred(scheduledCommandCaptor.capture());
    scheduledCommandCaptor.getValue().execute();
    verify(mockTimerFactory).schedule(runnableCaptor.capture(), eq(FADE_DURATION_MS));
    runnableCaptor.getValue().run();
    verify(mockView).remove(mockAlertView1);
  }

  @Test public void fadingAlert_mouseOver_stopsFading() {
    Alert alert = controller.newStatusAlertBuilder(mockAlertViewContents1).create();
    alert.requestDisplay();

    verify(mockScheduler).scheduleDeferred(scheduledCommandCaptor.capture());
    scheduledCommandCaptor.getValue().execute();

    verify(mockAlertView1).addMouseOverHandler(mouseOverHandlerCaptor.capture());
    reset(mockAlertView1);
    mouseOverHandlerCaptor.getValue().onMouseOver(null);
    verify(mockTimerHandle).cancel();
    verify(mockAlertView1).removeStyleName(CSS.fade());
  }

  @Test public void fadingAlert_mouseOut_restartsFading() {
    Alert alert = controller.newStatusAlertBuilder(mockAlertViewContents1).create();
    alert.requestDisplay();

    verify(mockScheduler).scheduleDeferred(scheduledCommandCaptor.capture());
    scheduledCommandCaptor.getValue().execute();

    verify(mockAlertView1).addMouseOverHandler(mouseOverHandlerCaptor.capture());
    verify(mockAlertView1).addMouseOutHandler(mouseOutHandlerCaptor.capture());
    mouseOverHandlerCaptor.getValue().onMouseOver(null);

    reset(mockAlertView1, mockScheduler);
    mouseOutHandlerCaptor.getValue().onMouseOut(null);

    verify(mockScheduler).scheduleDeferred(scheduledCommandCaptor.capture());
    scheduledCommandCaptor.getValue().execute();
    verify(mockAlertView1).addStyleName(CSS.fade());
  }

  private class TestableDefaultAlertController extends DefaultAlertController {
    TestableDefaultAlertController() {
      super(
          mockTimerFactory,
          mockScheduler,
          mockViewContainer,
          mockView,
          MoreProviders.of(mockAlertView1, mockAlertView2, mockAlertView3),
          RESOURCES);
    }

    @Override ForIsWidget getRootPanel() {
      return mockRootPanel;
    }
  }
}
