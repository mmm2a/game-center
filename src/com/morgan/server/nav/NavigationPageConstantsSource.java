package com.morgan.server.nav;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.morgan.server.constants.PageConstants;
import com.morgan.server.constants.PageConstantsSource;
import com.morgan.shared.common.DictionaryConstant;
import com.morgan.shared.nav.NavigationConstant;

/**
 * {@link PageConstantsSource} for the navigation package.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class NavigationPageConstantsSource implements PageConstantsSource<DictionaryConstant> {

  private final Provider<HttpServletRequest> requestProvider;
  
  @Inject NavigationPageConstantsSource(Provider<HttpServletRequest> requestProvider) {
    this.requestProvider = requestProvider;
  }
  
  @Override public void provideConstantsInto(PageConstants constantsSink) {
    constantsSink.add(NavigationConstant.APPLICATION_URL, requestProvider.get().getRequestURI());
  }
}
