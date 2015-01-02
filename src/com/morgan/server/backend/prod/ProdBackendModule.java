package com.morgan.server.backend.prod;

import com.google.inject.AbstractModule;
import com.morgan.server.backend.UserBackend;

/**
 * GUICE module for the production backends.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class ProdBackendModule extends AbstractModule {
	@Override protected void configure() {
		bind(UserBackend.class).to(ProdUserBackend.class);
	}
}
