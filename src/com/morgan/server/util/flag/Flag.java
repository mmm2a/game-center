package com.morgan.server.util.flag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that is used to describe flags (as methods in the {@link FlagAccessor}
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Flag {

  /** The name of the flag */
  String name();

  /** A short description for the flag */
  String description();

  /** Indicates whether or not the flag is required */
  boolean required() default true;

  /** A {@link FlagValueParser} to use to parse string representations */
  Class<? extends FlagValueParser> parser() default DefaultFlagValueParser.class;

  /** A default value to assign to the flag if it isn't set (and isn't required) */
  String defaultValue() default "";
}
