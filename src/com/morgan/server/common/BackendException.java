package com.morgan.server.common;

/**
 * Generic exception class for exceptions that come from the server.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class BackendException extends Exception {

	static final long serialVersionUID = 0L;
	
	public BackendException(String fmt, Object...args) {
		super(String.format(fmt, args));
	}
	
	public BackendException(Throwable cause, String fmt, Object...args) {
		super(String.format(fmt, args), cause);
	}
}
