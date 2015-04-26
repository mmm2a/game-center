package com.morgan.client.alert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

/**
 * Collection of GIN bindings common for alerts.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class CommonAlertBindings {

  private CommonAlertBindings() {
    // Do not instantiate.
  }

  @BindingAnnotation
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
  public @interface Loading {
  }
}
