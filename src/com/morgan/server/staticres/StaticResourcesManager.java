package com.morgan.server.staticres;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import com.google.common.net.MediaType;
import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.Reflection;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morgan.server.util.mime.MediaTypeMapper;

/**
 * A manager (and implementation of the {@link StaticResourcesFactory} interface) for managing and
 * tracking static resources.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
class StaticResourcesManager implements StaticResourcesFactory {

  static final String PATH_PREFIX = "/res/";

  /**
   * An interface to a resource that the {@link StaticResourcesHttpServlet} can use to fill
   * responses.
   *
   * @author mark@mark-morgan.net (Mark Morgan)
   */
  interface ResourceSource {
    void doGet(HttpServletResponse response) throws IOException;
  }

  private final Function<String, MediaType> mediaTypeMapper;

  private final Map<String, Map<String, ResourceSource>> registeredResources = new HashMap<>();
  private final Map<Class<? extends StaticResources>, StaticResources> cachedProxies =
      new HashMap<>();

  @Inject StaticResourcesManager(@MediaTypeMapper Function<String, MediaType> contentTypeMapper) {
    this.mediaTypeMapper = contentTypeMapper;
  }

  private static String discoverNameFromPath(String path) {
    int lastIndex = path.lastIndexOf('/');
    if (lastIndex >= 0) {
      path = path.substring(lastIndex + 1);
    }
    return path;
  }

  @VisibleForTesting ByteSource getByteSourceFor(Class<?> contextClass, String path) {
    return Resources.asByteSource(Resources.getResource(contextClass, path));
  }

  private void addResourcesToMapFrom(
      Class<?> resourcesInterface, Map<String, ResourceSource> resourcesMap) {
    for (Method method : resourcesInterface.getMethods()) {
      StaticResource annot = method.getAnnotation(StaticResource.class);
      Preconditions.checkState(annot != null,
          "Method %s is not annoated with a StaticResource annotation", method);
      Class<?> returnType = method.getReturnType();
      Preconditions.checkState(SafeUri.class.isAssignableFrom(returnType),
          "Method %s is a static resource method, but doesn't return SafeUri", method);
      Preconditions.checkState(method.getParameterTypes().length == 0,
          "Method %s is a static resource method, but has unanticipated parameters", method);

      String contentType = annot.contentType();
      String name = annot.name();
      String path = annot.value();
      Preconditions.checkState(!path.isEmpty(),
          "Static resource described by %s must have a non-empty path", method);
      ByteSource byteSource = getByteSourceFor(resourcesInterface, path);
      try {
        Preconditions.checkState(!byteSource.isEmpty(),
            "Static resource described by %s seems to be empty", method);
      } catch (IOException ioe) {
        throw new IllegalStateException(
            String.format("Unable to open resource described by %s", method),
            ioe);
      }

      if (name.isEmpty()) {
        name = discoverNameFromPath(path);
      }

      if (contentType.isEmpty()) {
        contentType = mediaTypeMapper.apply(name).toString();
      }

      resourcesMap.put(name, new ResourceSourceImpl(contentType, byteSource));
    }
  }

  private <T extends StaticResources> T createProxyFor(String context, Class<T> iface) {
    return Reflection.newProxy(iface, new ResourcesInvocationHandler(context));
  }

  /**
   * Gets the {@link ResourceSource} that matches a given context and name.  If none match, then
   * {@code null} is returned instead.
   */
  @Nullable ResourceSource getResource(String context, String name) {
    synchronized(registeredResources) {
      Map<String, ResourceSource> resourceMap = registeredResources.get(context);
      if (resourceMap != null) {
        return resourceMap.get(name);
      }
    }

    return null;
  }

  @Override public <T extends StaticResources> T createProxy(Class<T> resourcesInterface) {
    Preconditions.checkArgument(resourcesInterface.isInterface());

    synchronized(registeredResources) {
      StaticResources proxy = cachedProxies.get(resourcesInterface);
      if (proxy != null) {
        return resourcesInterface.cast(proxy);
      }

      String context = Integer.toHexString(resourcesInterface.getName().hashCode());
      Map<String, ResourceSource> resourceMap = registeredResources.get(context);
      if (resourceMap == null) {
        resourceMap = new HashMap<>();
        registeredResources.put(context, resourceMap);
      }

      addResourcesToMapFrom(resourcesInterface, resourceMap);

      T result = createProxyFor(context, resourcesInterface);
      cachedProxies.put(resourcesInterface, result);
      return result;
    }
  }

  private static final class ResourceSourceImpl implements ResourceSource {

    private final String contentType;
    private final ByteSource source;

    ResourceSourceImpl(String contentType, ByteSource source) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(contentType));
      this.contentType = contentType;
      this.source = Preconditions.checkNotNull(source);
    }

    @Override public void doGet(HttpServletResponse resp) throws IOException {
      try (OutputStream out = resp.getOutputStream()) {
        resp.setContentType(contentType);

        source.copyTo(out);
      }
    }
  }

  private static final class ResourcesInvocationHandler extends AbstractInvocationHandler {

    private final String context;

    ResourcesInvocationHandler(String context) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(context));
      this.context = context;
    }

    @Override protected Object handleInvocation(
        Object proxy,
        Method method, Object[] args) throws Throwable {
      StaticResource resource = Preconditions.checkNotNull(
          method.getAnnotation(StaticResource.class));
      String name = resource.name();
      if (name.isEmpty()) {
        name = discoverNameFromPath(resource.value());
      }

      return UriUtils.fromString(PATH_PREFIX + context + "/" + name);
    }
  }
}
