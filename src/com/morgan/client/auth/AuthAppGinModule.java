package com.morgan.client.auth;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.multibindings.GinMapBinder;
import com.google.gwt.inject.client.multibindings.GinMultibinder;
import com.morgan.client.common.CommonBindingAnnotations.Default;
import com.morgan.client.common.CommonBindingAnnotations.PagePresenters;
import com.morgan.client.common.CommonGinModule;
import com.morgan.client.page.PagePresenter;
import com.morgan.shared.auth.AuthApplicationPlace;
import com.morgan.shared.auth.AuthApplicationPlaceRepresentation;
import com.morgan.shared.nav.ApplicationPlace;
import com.morgan.shared.nav.ApplicationPlaceRepresentation;

/**
 * GIN module for the authentication application.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AuthAppGinModule extends AbstractGinModule {

  @Override protected void configure() {
    install(new CommonGinModule());

    bind(ApplicationPlace.class).annotatedWith(Default.class).to(AuthApplicationPlace.class);

    GinMultibinder.newSetBinder(binder(), ApplicationPlaceRepresentation.class)
        .addBinding().to(AuthApplicationPlaceRepresentation.class);

    GinMapBinder.newMapBinder(binder(), Class.class, PagePresenter.class, PagePresenters.class)
        .addBinding(AuthApplicationPlace.class).to(AuthPagePresenter.class);
  }
}