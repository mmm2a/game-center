package com.morgan.client.nav;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.morgan.client.constants.ClientPageConstants;
import com.morgan.shared.nav.ClientApplication;
import com.morgan.shared.nav.NavigationConstant;
import com.morgan.shared.nav.UrlCreator;

/**
 * GIN module for the client nav package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class NavGinModule extends AbstractGinModule {

  @Override protected void configure() {
    bind(DefaultNavigation.class).asEagerSingleton();
    bind(NavigationState.class).to(DefaultNavigation.class);
    bind(Navigator.class).to(DefaultNavigation.class);
    bind(UrlCreator.class).to(ClientUrlCreator.class);
  }

  @Provides protected ClientApplication provideClientApplication(ClientPageConstants constants) {
    String appType = constants.getString(NavigationConstant.APPLICATION_TYPE);
    Preconditions.checkArgument(!Strings.isNullOrEmpty(appType));
    return ClientApplication.valueOf(appType);
  }
}
