package com.morgan.server.util.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation type to annotate {@link AdvancedLogger} instances in clases.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface InjectLogger {
}
