package com.morgan.server.util.mime;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.google.common.net.MediaType;

/**
 * Tests for the {@link MediaTypeMapperImpl} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class MediaTypeMapperImplTest {

  private MediaTypeMapperImpl impl;

  @Before public void createTestInstances() throws Exception {
    impl = new MediaTypeMapperImpl();
  }

  @Test public void apply_nullInput_usesDefault() {
    assertThat(impl.apply(null)).isEqualTo(MediaTypeMapperImpl.DEFAULT_CONTENT_TYPE);
  }

  @Test public void apply_noExtension_usesDefault() {
    assertThat(impl.apply("no-extension")).isEqualTo(MediaTypeMapperImpl.DEFAULT_CONTENT_TYPE);
  }

  @Test public void apply() {
    assertThat(impl.apply("foo.html")).isEqualTo(MediaType.create("text", "html"));
  }
}
