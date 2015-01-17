package com.morgan.client.auth;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.multibindings.GinMultibinder;
import com.morgan.client.common.CommonBindingAnnotations.Default;
import com.morgan.client.common.CommonGinModule;
import com.morgan.shared.auth.LoginApplicationPlace;
import com.morgan.shared.auth.LoginApplicationPlaceRepresentation;
import com.morgan.shared.nav.ApplicationPlaceRepresentation;

/**
 * GIN module for the authentication application.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AuthAppGinModule extends AbstractGinModule {

  @Override protected void configure() {
    install(new CommonGinModule());

    bind(LoginApplicationPlace.class).annotatedWith(Default.class).to(LoginApplicationPlace.class);

    GinMultibinder.newSetBinder(binder(), ApplicationPlaceRepresentation.class)
        .addBinding().to(LoginApplicationPlaceRepresentation.class);
  }
}