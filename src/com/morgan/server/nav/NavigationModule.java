package com.morgan.server.nav;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import com.morgan.server.constants.PageConstantsSource;
import com.morgan.shared.nav.ClientApplication;

/**
 * GUICE module for the navigation package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class NavigationModule extends AbstractModule {

  private static final String APPS_PREFIX = "/apps/";

  @Override protected void configure() {
    Multibinder.newSetBinder(binder(), PageConstantsSource.class)
        .addBinding().to(NavigationPageConstantsSource.class);
  }

  @Provides protected ClientApplication provideClientApplication(
      Provider<HttpServletRequest> requestProvider) {
    ClientApplication clientApp = null;

    try {
      URL url = new URL(requestProvider.get().getRequestURL().toString());
      String path = url.getPath();
      Preconditions.checkState(path.startsWith(APPS_PREFIX));
      path = path.substring(APPS_PREFIX.length());
      clientApp = ClientApplication.fromPathComponent(path);
    } catch (MalformedURLException e) {
      // this shouldn't happen
      Throwables.propagate(e);
    }

    return Preconditions.checkNotNull(clientApp);
  }
}
