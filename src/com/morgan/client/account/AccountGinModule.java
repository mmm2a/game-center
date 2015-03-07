package com.morgan.client.account;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.multibindings.GinMapBinder;
import com.google.gwt.inject.client.multibindings.GinMultibinder;
import com.morgan.client.common.CommonBindingAnnotations.PagePresenters;
import com.morgan.client.page.PagePresenter;
import com.morgan.shared.account.AccountCreationApplicationPlace;
import com.morgan.shared.account.AccountCreationPlaceRepresentation;
import com.morgan.shared.nav.ApplicationPlaceRepresentation;

/**
 * GIN module for the account pages.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AccountGinModule extends AbstractGinModule {

  @Override protected void configure() {
    GinMultibinder.newSetBinder(binder(), ApplicationPlaceRepresentation.class)
        .addBinding().to(AccountCreationPlaceRepresentation.class);

    GinMapBinder.newMapBinder(binder(), Class.class, PagePresenter.class, PagePresenters.class)
        .addBinding(AccountCreationApplicationPlace.class).to(AccountCreationPagePresenter.class);
  }
}
