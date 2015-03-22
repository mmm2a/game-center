package com.morgan.server.util.stat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark which methods should have statistics measured for them.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Retention(RetentionPolicy.RUNTIME) @Target({ ElementType.METHOD })
public @interface MeasureStatistics {
}
