package com.morgan.server.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.morgan.shared.common.Role;

/**
 * An annotation applied to a method that ensures that only a user authorized to call that method
 * on the server is permitted to do so.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AuthorizedFor {

  /** Who the call is authorized for */
  Role value();
}
