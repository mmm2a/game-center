package com.morgan.client.nav;

import com.google.common.base.Preconditions;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.inject.Inject;
import com.morgan.client.common.CommonBindingAnnotations.Default;
import com.morgan.client.page.PagePresenterHelper;
import com.morgan.shared.nav.ApplicationPlace;

/**
 * Default implementation of both the {@link Navigator} and {@link NavigationState} interfaces.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class DefaultNavigation implements Navigator, NavigationState {

  private final ValueChangeHandler<String> historyChangeHandler =
      new ValueChangeHandler<String>() {
        @Override public void onValueChange(ValueChangeEvent<String> event) {
          onPathChanged(event.getValue());
        }
      };

  private final ApplicationPlace defaultApplicationPlace;
  private final HistoryHelper helper;
  private final PlaceRepresentationHelper representationHelper;
  private final PagePresenterHelper pagePresentationHelper;

  private ApplicationPlace currentPlace;

  @Inject DefaultNavigation(
      @Default ApplicationPlace defaultApplicationPlace,
      HistoryHelper helper,
      PlaceRepresentationHelper representationHelper,
      PagePresenterHelper pagePresentationHelper) {
    this.defaultApplicationPlace = defaultApplicationPlace;
    this.helper = helper;
    this.representationHelper = representationHelper;
    this.pagePresentationHelper = pagePresentationHelper;

    helper.addValueChangeHandler(historyChangeHandler);

    onPathChanged(helper.getToken());
  }

  private void onPathChanged(String path) {
    currentPlace = representationHelper.parseFromHistoryToken(path);
    if (currentPlace != null) {
      pagePresentationHelper.presentPageFor(currentPlace);
    } else {
      helper.replaceItem(
          representationHelper.representPlaceAsHistoryToken(defaultApplicationPlace), true);
    }
  }

  @Override public ApplicationPlace getCurrentPlace() {
    return currentPlace;
  }

  @Override public void navigateTo(ApplicationPlace place) {
    Preconditions.checkNotNull(place);
    helper.newItem(representationHelper.representPlaceAsHistoryToken(place), true);
  }

  @Override public void back() {
    helper.back();
  }

  @Override public void forward() {
    helper.forward();
  }
}
