package com.morgan.server.feature;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import com.morgan.server.util.flag.FlagAccessorFactory;
import com.morgan.shared.feature.FeatureChecker;

/**
 * GUICE module for configuring classes having to do with the server's version of the feature
 * package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class FeaturesModule extends AbstractModule {

  private void addServerFeatureEvaluators() {
    Multibinder<ServerFeatureEvaluator> evaluators = Multibinder.newSetBinder(
        binder(), ServerFeatureEvaluator.class);

    install(new FactoryModuleBuilder().build(FeatureConfiguration.Factory.class));

    evaluators.addBinding().to(SimpleEnableDisableEvaluator.class);
  }

  @Override protected void configure() {
    bind(FeatureChecker.class).to(ServerFeatureChecker.class);

    addServerFeatureEvaluators();
  }

  @Provides @Singleton protected FeatureFlagAccessor provideFeatureFlagAccessor(
      FlagAccessorFactory flagAccessorFactory) {
    return flagAccessorFactory.getFlagAccessor(FeatureFlagAccessor.class);
  }
}
