package com.morgan.shared.common;

/**
 * Exception type thrown when a user tries to make a call on the server that s/he doesn't have
 * permission for.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class PermissionDeniedException extends BackendException {

  static final long serialVersionUID = 1L;

  public PermissionDeniedException() {
  }
}
