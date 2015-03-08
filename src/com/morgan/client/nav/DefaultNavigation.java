package com.morgan.client.nav;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window.Location;
import com.google.inject.Inject;
import com.google.inject.Provider;
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

  private final LocationHelper locationHelper;
  private final Provider<ApplicationPlace> defaultApplicationPlaceProvider;
  private final HistoryHelper helper;
  private final PlaceRepresentationHelper representationHelper;
  private final PagePresenterHelper pagePresentationHelper;
  private final UrlCreator urlCreator;

  @VisibleForTesting ApplicationPlace currentPlace;

  @Inject DefaultNavigation(
      Scheduler scheduler,
      LocationHelper locationHelper,
      @Default Provider<ApplicationPlace> defaultApplicationPlaceProvider,
      final HistoryHelper helper,
      PlaceRepresentationHelper representationHelper,
      UrlCreator urlCreator,
      PagePresenterHelper pagePresentationHelper) {
    this.locationHelper = locationHelper;
    this.defaultApplicationPlaceProvider = defaultApplicationPlaceProvider;
    this.helper = helper;
    this.representationHelper = representationHelper;
    this.pagePresentationHelper = pagePresentationHelper;
    this.urlCreator = urlCreator;

    helper.addValueChangeHandler(historyChangeHandler);

    // We need to defer schedule the first call to bootstrap the app as it requires in some cases
    // that the navigator is already created (which can't happen until this constructor returns).
    scheduler.scheduleDeferred(new ScheduledCommand() {
      @Override public void execute() {
        onPathChanged(helper.getToken());
      }
    });
  }

  private void onPathChanged(String path) {
    currentPlace = representationHelper.parseFromHistoryToken(path);
    if (currentPlace != null) {
      pagePresentationHelper.presentPageFor(currentPlace);
    } else {
      helper.replaceItem(representationHelper.representPlaceAsHistoryToken(
          defaultApplicationPlaceProvider.get()), true);
    }
  }

  @Override public ApplicationPlace getCurrentPlace() {
    return currentPlace;
  }

  @Override public void navigateTo(ApplicationPlace place) {
    Preconditions.checkNotNull(place);
    if (currentPlace.getClientApplication() == place.getClientApplication()) {
      helper.newItem(representationHelper.representPlaceAsHistoryToken(place), true);
    } else {
      locationHelper.assign(urlCreator.createUrlFor(place));
    }
  }

  @Override public void back() {
    helper.back();
  }

  @Override public void forward() {
    helper.forward();
  }

  @Override public void reload() {
    Location.reload();
  }
}
