package com.morgan.server.backend.prod;

import com.google.inject.AbstractModule;
import com.morgan.server.backend.AlarmBackend;
import com.morgan.server.backend.UserBackend;
import com.morgan.server.db.DatabaseModule;

/**
 * GUICE module for the production backends.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class ProdBackendModule extends AbstractModule {
	@Override protected void configure() {
	  install(new DatabaseModule());

		bind(UserBackend.class).to(ProdUserBackend.class);
		bind(AlarmBackend.class).to(ProdAlarmBackend.class);
	}
}
