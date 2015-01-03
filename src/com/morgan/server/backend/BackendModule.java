package com.morgan.server.backend;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.morgan.server.backend.fake.FakeBackendModule;
import com.morgan.server.backend.prod.ProdBackendModule;
import com.morgan.server.util.flag.FlagAccessorFactory;

/**
 * GUICE module for configuring the backends.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class BackendModule extends AbstractModule {

	@Override protected void configure() {
		BackendType backendType = BackendType.getCurrent();

		switch (backendType) {
			case FAKE :
				install(new FakeBackendModule());
				break;

			case PROD :
				install(new ProdBackendModule());
				break;

			default :
				throw new IllegalStateException("Unknown backend type: " + backendType);
		}
	}

	@Provides @Singleton BackendFlagAccessor provideBackendFlagAccessor(
	    FlagAccessorFactory accessorFactory) {
	  return accessorFactory.getFlagAccessor(BackendFlagAccessor.class);
	}
}
