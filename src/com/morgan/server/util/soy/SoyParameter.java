package com.morgan.server.util.soy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Gives necessary information about a parameter to a soy template method.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface SoyParameter {
  /** Identifies the soy parameter name for this parameter */
  String name();
  
  Class<? extends SoyDataConverter> converter() default DefaultSoyDataConverter.class;
}
