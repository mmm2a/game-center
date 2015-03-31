package com.morgan.server.staticres;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identified a single static resource that should be served.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
  ElementType.METHOD
  })
public @interface StaticResource {
  String value();
  String name() default "";
  String contentType() default "";
}
