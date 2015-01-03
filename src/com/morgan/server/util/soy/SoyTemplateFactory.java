package com.morgan.server.util.soy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;
import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.Reflection;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.template.soy.SoyFileSet;
import com.google.template.soy.data.SanitizedContent;
import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.tofu.SoyTofu;
import com.google.template.soy.tofu.SoyTofu.Renderer;

/**
 * Factory class for creating dynamically generated proxies for {@link SoyTemplate} interfaces.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
// TODO(markmorgan): We need the deprecation suppressing because of the use of input supplier.
@SuppressWarnings("deprecation")
public class SoyTemplateFactory {

  private final Injector injector;
  private final Provider<SoyFileSet.Builder> soyFileSetBuilderProvider;
  
  @Inject SoyTemplateFactory(
          Injector injector,
          Provider<SoyFileSet.Builder> soyFileSetBuilderProvider) {
    this.injector = injector;
    this.soyFileSetBuilderProvider = soyFileSetBuilderProvider;
  }
  
  private ImmutableMap<Method, SoyMethodCaller> createSoyMethodCallerMapFor(
      SoyTofu tofu, Class<?> iface) {
    ImmutableMap.Builder<Method, SoyMethodCaller> builder = ImmutableMap.builder();
    
    for (Method m : iface.getMethods()) {
      builder.put(m, new SoyMethodCaller(tofu, m));
    }
    
    return builder.build();
  }
  
  private void addResourceToSoyFileSetBuilder(
      SoyFileSet.Builder builder, Class<?> context, String resource) {
    // TODO(markmorgan): When we get a new version of SOY, this is what we want, but SOY is
    // a few versions behind in Guava and so this won't work
    // builder.add(Resources.getResource(context, resource));
    final URL url = Resources.getResource(context, resource);
    builder.add(new InputSupplier<Reader>() {
      @Override public Reader getInput() throws IOException {
        InputStream in = url.openStream();
        return new InputStreamReader(in, Charsets.UTF_8);
      }
    }, url.toString());
  }
  
  /**
   * Creates a proxy instance of the input interface.
   */
  public <T extends SoyTemplate> T createSoyTemplate(Class<T> templateIface) {
    Preconditions.checkArgument(templateIface.isInterface());
    
    Soy soy = templateIface.getAnnotation(Soy.class);
    Preconditions.checkState(
            soy != null, "SoyTemplate interface %s doesn't have a Soy annotation", templateIface);
    Preconditions.checkState(!Strings.isNullOrEmpty(soy.namespace()),
            "Soy annotation on %s cannot have an empty namespace", templateIface);
    
    String file = soy.file();
    Class<?> context = soy.resourceContext();
    String resource = soy.resource();
    
    Preconditions.checkState(
        (!file.isEmpty() && resource.isEmpty()) || (file.isEmpty() && !resource.isEmpty()),
        "Soy interface %s cannot have both a file and a resource indicated in the Soy annotation",
        templateIface);
    
    SoyFileSet.Builder builder = soyFileSetBuilderProvider.get();
    
    if (file.isEmpty()) {
      if (context.equals(Object.class)) {
        context = templateIface;
      }
      
      addResourceToSoyFileSetBuilder(builder, context, resource);
    } else {
      builder.add(new File(file));
    }
    
    SoyFileSet fileSet = builder.build();
    SoyTofu tofu = fileSet.compileToTofu();
    tofu = tofu.forNamespace(soy.namespace());
    
    return Reflection.newProxy(
        templateIface,
        new SoyTemplateInvocationHandler(createSoyMethodCallerMapFor(tofu, templateIface)));
  }
  
  @Nullable private SoyParameter getSoyParameterAnnotation(Annotation[] parameterAnnotations) {
    for (Annotation a : parameterAnnotations) {
      if (a instanceof SoyParameter) {
        return (SoyParameter) a;
      }
    }
    
    return null;
  }
  
  /**
   * Interface for an internal type that does the final rendering state for a method.  This can
   * be used to return the appropriate type for a method.
   */
  private interface FinalRenderer {
    /**
     * Perform the final rendering stage.
     */
    @Nullable Object render(Renderer renderer);
  }
  
  /**
   * Internal class that is used to call a soy template method given a set of
   * input parameters.
   * 
   * @author mark@mark-morgan.net (Mark Morgan)
   */
  private class SoyMethodCaller {
    
    private final SoyTofu tofu;
    private final String methodName;
    private final String[] parameterNames;
    private final SoyDataConverter[] parameterConverters;
    private final FinalRenderer finalRenderer;
    
    SoyMethodCaller(SoyTofu tofu, Method method) {
      this.tofu = Preconditions.checkNotNull(tofu);
      SoyMethod annotation = method.getAnnotation(SoyMethod.class);
      if (annotation != null) {
        this.methodName = "." + annotation.name();
      } else {
        this.methodName = "." + method.getName();
      }
      
      Class<?> mRetT = method.getReturnType();
      if (mRetT.isAssignableFrom(String.class)) {
        finalRenderer = new FinalRenderer() {
          @Override public Object render(Renderer renderer) {
            return renderer.render();
          }
        };
      } else if (mRetT.isAssignableFrom(SanitizedContent.class)) {
        finalRenderer = new FinalRenderer() {
          @Override public Object render(Renderer renderer) {
            return renderer.renderAsSanitizedContent();
          }
        };
      } else if (mRetT.isAssignableFrom(Renderer.class)) {
        finalRenderer = new FinalRenderer() {
          @Override public Object render(Renderer renderer) {
            return renderer;
          }
        };
      } else if (mRetT.isAssignableFrom(SafeHtml.class)) {
        finalRenderer = new FinalRenderer() {
          @Override public Object render(Renderer renderer) {
            return SafeHtmlUtils.fromTrustedString(
                    renderer.renderAsSanitizedContent().getContent());
          }
        };
      } else {
        throw new IllegalStateException(String.format(
                "Unable to create final renderer for return type of soy method %s", method));
      }
      
      Annotation[][] pAnnotations = method.getParameterAnnotations();
      parameterNames = new String[pAnnotations.length];
      parameterConverters = new SoyDataConverter[pAnnotations.length];
      
      for (int i = 0; i < pAnnotations.length; i++) {
        SoyParameter pa = getSoyParameterAnnotation(pAnnotations[i]);
        Preconditions.checkState(pa != null,
                "Unable to find SoyParameter annotation on parameter %d in method %s", i, method);
        
        parameterNames[i] = pa.name();
        parameterConverters[i] = injector.getInstance(pa.converter());
      }
    }
    
    @Nullable Object callSoyMethodWith(Object[] args) {
      Preconditions.checkState(args.length == parameterNames.length);
      SoyMapData mapData = new SoyMapData();
      for (int i = 0; i < args.length; i++) {
        parameterConverters[i].addToSoyMapData(mapData, parameterNames[i], args[i]);
      }
      
      Renderer renderer = tofu.newRenderer(methodName)
          .setData(mapData);
      return finalRenderer.render(renderer);
    }
  }
  
  /**
   * Internal {@link InvocationHandler} implementation for soy templates.
   */
  private static class SoyTemplateInvocationHandler extends AbstractInvocationHandler {

    private final ImmutableMap<Method, SoyMethodCaller> methodCallerMap;
    
    SoyTemplateInvocationHandler(Map<Method, SoyMethodCaller> methodCallerMap) {
      this.methodCallerMap = ImmutableMap.copyOf(methodCallerMap);
    }
    
    @Override protected Object handleInvocation(Object proxy, Method method, Object[] args)
        throws Throwable {
      return methodCallerMap.get(method).callSoyMethodWith(args);
    }
  }
}
