package com.morgan.client.nav;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.morgan.client.constants.ClientPageConstants;
import com.morgan.shared.nav.ApplicationPlace;
import com.morgan.shared.nav.NavigationConstant;

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
      
  private final ClientPageConstants constants;
  
  @Inject DefaultNavigation(HistoryHelper helper, ClientPageConstants constants) {
    this.constants = constants;
    
    helper.addValueChangeHandler(historyChangeHandler);
    helper.fireCurrentHistoryState();
  }
  
  private void onPathChanged(String path) {
    Window.alert("Server URL: " + constants.getString(NavigationConstant.APPLICATION_URL)
        + ", Token: " + path);
  }
  
  @Override public ApplicationPlace getCurrentPlace() {
    // TODO(morgan)
    return null;
  }

  @Override public void navigateTo(ApplicationPlace place) {
  }

  @Override public void back() {
  }

  @Override public void forward() {
  }
}
