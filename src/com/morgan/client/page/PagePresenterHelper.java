package com.morgan.client.page;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.morgan.client.common.CommonBindingAnnotations.PagePresenters;
import com.morgan.shared.nav.ApplicationPlace;

/**
 * A helper class for helping present the correct page for a given place and for managing the page
 * state in the browser.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
public class PagePresenterHelper {

  @SuppressWarnings("rawtypes")
  private final Provider<Map<Class, PagePresenter>> pagePresentersMapProvider;

  @Nullable private IsWidget currentPage;

  @SuppressWarnings("rawtypes")
  @Inject PagePresenterHelper(
      @PagePresenters Provider<Map<Class, PagePresenter>> pagePresentersMapProvider) {
    this.pagePresentersMapProvider = pagePresentersMapProvider;
  }

  @VisibleForTesting HasWidgets.ForIsWidget getRootPanel() {
    return RootPanel.get();
  }

  @VisibleForTesting void removeWidgetFromParent(IsWidget widgetToRemove) {
    widgetToRemove.asWidget().removeFromParent();
  }

  /**
   * Asks this helper to find the correct presenter for the target place and to present the
   * page for it, removing any previously rendered page that might be there.
   */
  public <T extends ApplicationPlace> void presentPageFor(T place) {
    Preconditions.checkNotNull(place);
    @SuppressWarnings("unchecked")
    PagePresenter<T> presenter = pagePresentersMapProvider.get().get(place.getClass());
    Preconditions.checkState(presenter != null);

    // Remove the current page if its there
    if (currentPage != null) {
      removeWidgetFromParent(currentPage);
      currentPage = null;
    }

    Optional<? extends IsWidget> newPage =
        Preconditions.checkNotNull(presenter.presentPageFor(place));
    currentPage = newPage.orNull();
    if (currentPage != null) {
      getRootPanel().add(currentPage);
    }
  }
}
