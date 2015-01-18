package com.morgan.client.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

/**
 * Common binding annotations used in the client.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class CommonBindingAnnotations {

  private CommonBindingAnnotations() {
    // Do not instantiate
  }

  /**
   * An annotation representing the "default" version of something.
   *
   * @author mark@mark-morgan.net (Mark Morgan)
   */
  @BindingAnnotation
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
  public @interface Default {
  }

  @BindingAnnotation
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
  public @interface PagePresenters {
  }
}
