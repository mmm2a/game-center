package com.morgan.server.backend;

import com.morgan.server.util.flag.Flag;
import com.morgan.server.util.flag.FlagAccessor;

/**
 * {@link FlagAccessor} for the backends package.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
interface BackendFlagAccessor extends FlagAccessor {

	@Flag(name = "backend-type",
	    description = "Describes which backend to laucnh",
	    required = true)
	BackendType backendType();
}
