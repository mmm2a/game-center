package com.morgan.client.nav;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.inject.Inject;
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

  private final HistoryHelper helper;

  @Inject DefaultNavigation(HistoryHelper helper) {
    this.helper = helper;

    helper.addValueChangeHandler(historyChangeHandler);
    helper.fireCurrentHistoryState();
  }

  private void onPathChanged(String path) {
  }

  @Override public ApplicationPlace getCurrentPlace() {
    // TODO(morgan)
    return null;
  }

  @Override public void navigateTo(ApplicationPlace place) {
  }

  @Override public void back() {
    helper.back();
  }

  @Override public void forward() {
    helper.forward();
  }
}
