package com.morgan.server.util.soy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for a soy interface that describes required properties for the soy template
 * represented by this interface.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Soy {
  /** Identifies the namespace of the template file that is being loaded */
  String namespace();
  
  /**
   * Identifies a file in the local file system from which to load the soy template.
   * It is not permitted to specify both this annotation AND
   * the {@link #resource()}/{@link Soy#resourceContext()} attributes.
   */
  String file() default "";
  
  /**
   * Identifies the class relative to which resources will be loaded.  If this annotation is
   * not filled in, but resource loading is used, then the template interface's context is used.
   */
  Class<?> resourceContext() default Object.class;
  
  /**
   * Identifies a resource in the local VM from which to load the soy template.
   * It is not permitted to specify both this annotation AND
   * the {@link #file()} annotation.
   */
  String resource() default "";
}
