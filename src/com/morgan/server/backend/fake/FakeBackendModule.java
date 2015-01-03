package com.morgan.server.backend.fake;

import com.google.inject.AbstractModule;
import com.morgan.server.backend.UserBackend;

/**
 * GUICE module for the fake server variation.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class FakeBackendModule extends AbstractModule {
	@Override protected void configure() {
		bind(UserBackend.class).to(FakeUserBackend.class);
	}
}