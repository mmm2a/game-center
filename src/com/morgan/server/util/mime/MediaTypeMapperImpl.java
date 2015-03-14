package com.morgan.server.util.mime;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.activation.MimeType;
import javax.annotation.Nullable;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import com.google.common.net.MediaType;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Implementation of a {@link Function} that will map {@link String} instances representing file
 * names to {@link MimeType} instances.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
class MediaTypeMapperImpl implements Function<String, MediaType> {

  private static final String RESOURCE = "resources/content-map.properties";

  @VisibleForTesting static final MediaType DEFAULT_CONTENT_TYPE =
      MediaType.create("application", "octet-stream");

  private final Properties extensionToType;

  @Inject MediaTypeMapperImpl() throws IOException {
    extensionToType = new Properties();

    URL url = Resources.getResource(MediaTypeMapperImpl.class, RESOURCE);
    ByteSource source = Resources.asByteSource(url);
    try (InputStream in = source.openBufferedStream()) {
      extensionToType.load(in);
    }
  }

  @Override @Nullable public MediaType apply(@Nullable String input) {
    if (input == null) {
      return DEFAULT_CONTENT_TYPE;
    }

    String result = null;

    int extensionI = input.lastIndexOf('.');
    if (extensionI >= 0) {
      result = extensionToType.getProperty(input.substring(extensionI + 1));
    }

    if (Strings.isNullOrEmpty(result)) {
      return DEFAULT_CONTENT_TYPE;
    }

    return MediaType.parse(result);
  }
}
