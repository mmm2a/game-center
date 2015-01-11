package com.morgan.client.nav;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gwt.event.logical.shared.ValueChangeHandler;

/**
 * Tests for the {@link DefaultNavigation} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultNavigationTest {

  @Mock private HistoryHelper mockHistoryHelper;

  @Captor private ArgumentCaptor<ValueChangeHandler<String>> valueChangeHandlerCaptor;

  private DefaultNavigation navigation;

  @Before public void createTestInstances() {
    navigation = new DefaultNavigation(mockHistoryHelper);
  }

  @Test public void construction_addsChangeHandlerAndFiresFirstEvent() {
    InOrder order = inOrder(mockHistoryHelper);

    order.verify(mockHistoryHelper).addValueChangeHandler(valueChangeHandlerCaptor.capture());
    order.verify(mockHistoryHelper).fireCurrentHistoryState();
  }

  @Test public void back_callsThroughToHistoryHelper() {
    navigation.back();
    verify(mockHistoryHelper).back();
  }

  @Test public void forward_callsThroughToHistoryHelper() {
    navigation.forward();
    verify(mockHistoryHelper).forward();
  }
}
