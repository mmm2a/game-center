package com.morgan.client.game;

import com.google.gwt.inject.client.AbstractGinModule;
import com.morgan.client.account.AccountGinModule;
import com.morgan.client.alert.AlertGinModule;
import com.morgan.client.common.CommonBindingAnnotations.Default;
import com.morgan.client.common.CommonGinModule;
import com.morgan.client.game.home.HomeGinModule;
import com.morgan.shared.game.home.HomeApplicationPlace;
import com.morgan.shared.nav.ApplicationPlace;

/**
 * GIN module for the game application.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class GameAppGinModule extends AbstractGinModule {

  @Override protected void configure() {
    install(new AlertGinModule());
    install(new CommonGinModule());
    install(new HomeGinModule());
    install(new AccountGinModule());

    bind(ApplicationPlace.class).annotatedWith(Default.class).to(HomeApplicationPlace.class);
  }
}