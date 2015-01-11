package com.morgan.server.nav;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.morgan.server.constants.PageConstants;
import com.morgan.server.constants.PageConstantsSource;
import com.morgan.shared.nav.NavigationConstant;

/**
 * {@link PageConstantsSource} for the navigation package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class NavigationPageConstantsSource implements PageConstantsSource {

  private final Provider<HttpServletRequest> requestProvider;

  @Inject NavigationPageConstantsSource(Provider<HttpServletRequest> requestProvider) {
    this.requestProvider = requestProvider;
  }

  @Override public void provideConstantsInto(PageConstants constantsSink) {
    HttpServletRequest request = requestProvider.get();

    String url = request.getRequestURL().toString();
    int colon = url.indexOf(':');
    Preconditions.checkState(colon == 4 || colon == 5);

    constantsSink.add(NavigationConstant.APPLICATION_URL, url);
    constantsSink.add(NavigationConstant.APPLICATION_HOST, request.getServerName());
    constantsSink.add(NavigationConstant.APPLICATION_PORT, request.getServerPort());
    constantsSink.add(NavigationConstant.APPLICATION_PROTOCOL, url.substring(0, colon));
  }
}
