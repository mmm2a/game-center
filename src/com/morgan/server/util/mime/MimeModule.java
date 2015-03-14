package com.morgan.server.util.mime;

import com.google.common.base.Function;
import com.google.common.net.MediaType;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

/**
 * GUICE module for helping with mime types.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class MimeModule extends AbstractModule {

  @Override protected void configure() {
    bind(new TypeLiteral<Function<String, MediaType>>() {})
        .annotatedWith(MediaTypeMapper.class)
        .to(MediaTypeMapperImpl.class);
  }
}
