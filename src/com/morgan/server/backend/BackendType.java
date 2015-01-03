package com.morgan.server.backend;

import com.google.common.base.Strings;
import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import com.morgan.server.util.flag.Flags;

/**
 * Enumeration describing the types of backends available for execution.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public enum BackendType {
	FAKE,
	PROD;

	/**
	 * Retrieves the current backend type as specified by the command line flags.
	 */
	public static BackendType getCurrent() {
	  String rep = Flags.getInstance().getStringRepresentationFor("backend-type");
	  Preconditions.checkState(!Strings.isNullOrEmpty(rep), "Backend type not specified");

	  return BackendType.valueOf(rep);
  }
}
