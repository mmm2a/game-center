package com.morgan.client.page;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.util.Providers;
import com.morgan.shared.nav.ApplicationPlace;
import com.morgan.shared.nav.ApplicationPlaceRepresentation;
import com.morgan.shared.nav.ClientApplication;

/**
 * Tests for the {@link PagePresenterHelper} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class PagePresenterHelperTest {

  private static final PlaceClass1 PLACE1 = new PlaceClass1();
  private static final PlaceClass2 PLACE2 = new PlaceClass2();
  private static final PlaceClass3 PLACE3 = new PlaceClass3();

  @Mock private PagePresenter<ApplicationPlace> mockPagePresenter1;
  @Mock private PagePresenter<ApplicationPlace> mockPagePresenter2;
  @Mock private PagePresenter<ApplicationPlace> mockPagePresenter3;

  @Mock private HasWidgets.ForIsWidget mockRootPanel;

  @Mock private IsWidget mockPage1;
  @Mock private IsWidget mockPage2;
  @Mock private IsWidget mockPage3;

  private TestablePagePresenterHelper helper;

  @Before public void createTestInstances() {
    helper = new TestablePagePresenterHelper();
  }

  @Before public void setUpCommonMockInteractions() {
    doReturn(Optional.of(mockPage1)).when(mockPagePresenter1).presentPageFor(PLACE1);
    doReturn(Optional.of(mockPage2)).when(mockPagePresenter2).presentPageFor(PLACE2);
    doReturn(Optional.of(mockPage3)).when(mockPagePresenter3).presentPageFor(PLACE3);
  }

  @Test public void presentPageFor_rendersNewPage() {
    helper.presentPageFor(PLACE2);

    verify(mockRootPanel).add(mockPage2);
  }

  @Test public void presentPageFor_removesOldPage() {
    TestablePagePresenterHelper spyHelper = spy(helper);
    doNothing().when(spyHelper).removeWidgetFromParent(any(IsWidget.class));

    spyHelper.presentPageFor(PLACE1);
    verify(spyHelper, never()).removeWidgetFromParent(any(IsWidget.class));
    spyHelper.presentPageFor(PLACE2);
    verify(spyHelper).removeWidgetFromParent(mockPage1);
    spyHelper.presentPageFor(PLACE3);
    verify(spyHelper).removeWidgetFromParent(mockPage2);
  }

  @Test public void presentPageFor_absentPage_ignores() {
    doReturn(Optional.absent()).when(mockPagePresenter2).presentPageFor(PLACE2);
    TestablePagePresenterHelper spyHelper = spy(helper);
    doNothing().when(spyHelper).removeWidgetFromParent(any(IsWidget.class));

    spyHelper.presentPageFor(PLACE1);
    verify(spyHelper, never()).removeWidgetFromParent(any(IsWidget.class));
    spyHelper.presentPageFor(PLACE2);
    verify(spyHelper).removeWidgetFromParent(mockPage1);
    spyHelper.presentPageFor(PLACE3);
  }

  class TestablePagePresenterHelper extends PagePresenterHelper {

    @SuppressWarnings("rawtypes")
    TestablePagePresenterHelper() {
      super(Providers.<Map<Class, PagePresenter>>of(ImmutableMap.<Class, PagePresenter>of(
        PlaceClass1.class, mockPagePresenter1,
        PlaceClass2.class, mockPagePresenter2,
        PlaceClass3.class, mockPagePresenter3)));
    }

    @Override HasWidgets.ForIsWidget getRootPanel() {
      return mockRootPanel;
    }
  }

  private static class PlaceClass1 extends ApplicationPlace {

    protected PlaceClass1() {
      super(ClientApplication.AUTHENTICATION);
    }

    @Override public ApplicationPlaceRepresentation getRepresentation() {
      throw new UnsupportedOperationException();
    }
  }

  private static class PlaceClass2 extends ApplicationPlace {

    protected PlaceClass2() {
      super(ClientApplication.AUTHENTICATION);
    }

    @Override public ApplicationPlaceRepresentation getRepresentation() {
      throw new UnsupportedOperationException();
    }
  }

  private static class PlaceClass3 extends ApplicationPlace {

    protected PlaceClass3() {
      super(ClientApplication.AUTHENTICATION);
    }

    @Override public ApplicationPlaceRepresentation getRepresentation() {
      throw new UnsupportedOperationException();
    }
  }
}
