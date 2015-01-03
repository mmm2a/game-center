package com.morgan.server.util.soy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that allows a soy interface declration to override soy method information.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SoyMethod {
  /** The name (excluding the leading period) of the soy method in this template */
  String name();
}