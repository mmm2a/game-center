package com.morgan.shared.common;

/**
 * Generic exception class for exceptions that come from the server.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class BackendException extends Exception {

	static final long serialVersionUID = 0L;

	BackendException() {
	  // Default constructor for GWT
	}

	public BackendException(String msg) {
		super(msg);
	}

	public BackendException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
