package com.morgan.client.alert;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.morgan.client.alert.CommonAlertBindings.Loading;

/**
 * GIN module for alerts.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AlertGinModule extends AbstractGinModule {

  @Override protected void configure() {
  }

  @Loading @Provides @Singleton
  protected Alert provideLoadingAlert(AlertController alertController, AlertMessages messages) {
    return alertController.newStatusAlertBuilder(messages.loadingAlert())
        .isFading(false)
        .setPriority(10000)
        .create();
  }
}
