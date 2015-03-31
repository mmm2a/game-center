package com.morgan.server.staticres;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.annotation.Nullable;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Function;
import com.google.common.io.ByteSource;
import com.google.common.net.MediaType;
import com.google.gwt.safehtml.shared.SafeUri;
import com.morgan.testing.SafeUriSubject;

/**
 * Tests for the {@link StaticResourcesManager} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class StaticResourcesManagerTest {

  private static final Function<String, MediaType> CONTENT_TYPE_MAPPER =
      new Function<String, MediaType>() {
        @Override @Nullable public MediaType apply(@Nullable String input) {
          assertThat(input).endsWith(".png");
          return MediaType.PNG;
        }
      };

  private static final byte[] FOO_BYTES = new byte[] { 1, 2, 3, 4 };
  private static final byte[] BAR_BYTES = new byte[] { 5, 6, 7, 8 };
  private static final byte[] FOOBAR_BYTES = new byte[] { 9, 12, 11, 12 };

  interface TestResources extends StaticResources {
    @StaticResource("resources/foo.png")
    SafeUri fooPng();

    @StaticResource(
        value = "resources/bar.bin",
        name = "bar.png")
    SafeUri barPng();

    @StaticResource(
        value = "resources/foobar",
        name = "foo-bar",
        contentType = "image/png")
    SafeUri fooBar();
  }

  private static final String CONTEXT = Integer.toHexString(
      TestResources.class.getName().hashCode());

  @Mock private HttpServletResponse mockResponse;
  @Mock private ServletOutputStream mockOut;

  private TestResources resources;

  private TestableStaticResourcesManager manager;

  @Before public void createTestInstances() throws IOException {
    manager = new TestableStaticResourcesManager();
    resources = manager.createProxy(TestResources.class);
    when(mockResponse.getOutputStream()).thenReturn(mockOut);
  }

  @Test public void getResource_defaults() throws Exception {
    manager.getResource(CONTEXT, "foo.png").doGet(mockResponse);
    verify(mockOut).write(FOO_BYTES);
    verify(mockResponse).setContentType("image/png");
  }

  @Test public void getResource_nameOverridden() throws Exception {
    manager.getResource(CONTEXT, "bar.png").doGet(mockResponse);
    verify(mockOut).write(BAR_BYTES);
    verify(mockResponse).setContentType("image/png");
  }

  @Test public void getResource_contentTypeOverridden() throws Exception {
    manager.getResource(CONTEXT, "foo-bar").doGet(mockResponse);
    verify(mockOut).write(FOOBAR_BYTES);
    verify(mockResponse).setContentType("image/png");
  }

  @Test public void invocationHandler_defaults() {
    SafeUriSubject.assertThat(resources.fooPng()).containsString("/res/" + CONTEXT + "/foo.png");
  }

  @Test public void invocationHandler_nameOverridden() {
    SafeUriSubject.assertThat(resources.barPng()).containsString("/res/" + CONTEXT + "/bar.png");
  }

  @Test public void invocationHandler_contentTypeOverridden() {
    SafeUriSubject.assertThat(resources.fooBar()).containsString("/res/" + CONTEXT + "/foo-bar");
  }

  static class TestableStaticResourcesManager extends StaticResourcesManager {

    TestableStaticResourcesManager() {
      super(CONTENT_TYPE_MAPPER);
    }

    @Override ByteSource getByteSourceFor(Class<?> contextClass, String path) {
      assertThat(contextClass).isEqualTo(TestResources.class);

      switch (path) {
        case "resources/foo.png" :
          return ByteSource.wrap(FOO_BYTES);

        case "resources/bar.bin" :
          return ByteSource.wrap(BAR_BYTES);

        case "resources/foobar" :
          return ByteSource.wrap(FOOBAR_BYTES);
      }

      throw new IllegalArgumentException(String.format(
          "Didn't recognize resource path '%s'", path));
    }
  }
}
