package com.morgan.testing;

import java.lang.reflect.Method;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Primitives;
import com.google.common.reflect.AbstractInvocationHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.thirdparty.guava.common.base.Throwables;
import com.google.gwt.thirdparty.guava.common.reflect.Reflection;

/**
 * A factory for creating fake CSS instances for testing.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class FakeCssFactory<T extends CssResource> {
  
  private final Class<T> resourceClass;
  private final ImmutableMap.Builder<Method, Object> methodOverridesBuilder =
      ImmutableMap.builder();
  
  private FakeCssFactory(Class<T> resourceClass) {
    this.resourceClass = resourceClass;
    
    try {
      Method ensureInjected = resourceClass.getMethod("ensureInjected");
      methodOverridesBuilder.put(ensureInjected, true);
    } catch (NoSuchMethodException e) {
      // This shouldn't happen
      Throwables.propagate(e);
    }
  }
  
  public interface PartialMethodCall<T extends CssResource> {
    FakeCssFactory<T> thenReturn(Object value);
  }
  
  public PartialMethodCall<T> whenMethod(final String methodName) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(methodName));
    
    try {
      final Method method = resourceClass.getMethod(methodName);
      final Class<?> returnType = Primitives.wrap(method.getReturnType());
      return new PartialMethodCall<T>() {
        @Override public FakeCssFactory<T> thenReturn(Object value) {
          if (value != null) {
            Preconditions.checkState(returnType.isAssignableFrom(value.getClass()),
                String.format(
                    "Cannot return value of type %s from method %s.", value.getClass(), method));
          }
          
          methodOverridesBuilder.put(method, value);
          return FakeCssFactory.this;
        }
      };
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException(String.format(
          "Method %s does not exist in CSS class %s", methodName, resourceClass), e);
    }
  }
  
  public T createProxy() {
    return Reflection.newProxy(
        resourceClass, new CssProxyInvocationHandler(methodOverridesBuilder.build()));
  }
  
  public static <T extends CssResource> FakeCssFactory<T> forResource(Class<T> cssClass) {
    Preconditions.checkNotNull(cssClass);
    return new FakeCssFactory<>(cssClass);
  }
  
  private static class CssProxyInvocationHandler extends AbstractInvocationHandler {

    private final ImmutableMap<Method, Object> methodMap;
    
    CssProxyInvocationHandler(ImmutableMap<Method, Object> methodMap) {
      this.methodMap = methodMap;
    }
    
    @Override protected Object handleInvocation(
        Object proxy, Method method, Object[] args) throws Throwable {
      if (methodMap.containsKey(method)) {
        return methodMap.get(method);
      }
      
      Preconditions.checkState(method.getReturnType().equals(String.class),
          String.format("Don't know how to fake method %s", method));
      Preconditions.checkState(args.length == 0,
          String.format("Don't know how to fake method %s", method));
      
      return String.format("CSS[%s]", method.getName());
    }
  }
}
