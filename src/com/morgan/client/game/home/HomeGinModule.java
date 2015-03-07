package com.morgan.client.game.home;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.multibindings.GinMapBinder;
import com.google.gwt.inject.client.multibindings.GinMultibinder;
import com.morgan.client.common.CommonBindingAnnotations.PagePresenters;
import com.morgan.client.page.PagePresenter;
import com.morgan.shared.game.home.HomeApplicationPlace;
import com.morgan.shared.game.home.HomeApplicationPlaceRepresentation;
import com.morgan.shared.nav.ApplicationPlaceRepresentation;

/**
 * GIN module for the home package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class HomeGinModule extends AbstractGinModule {

  @Override protected void configure() {
    GinMultibinder.newSetBinder(binder(), ApplicationPlaceRepresentation.class)
        .addBinding().to(HomeApplicationPlaceRepresentation.class);

    GinMapBinder.newMapBinder(binder(), Class.class, PagePresenter.class, PagePresenters.class)
        .addBinding(HomeApplicationPlace.class).to(HomePagePresenter.class);
  }
}
