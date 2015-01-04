package com.morgan.server.util.flag;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.Reflection;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * Factory class for creating {@link FlagAccessor} instances.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class FlagAccessorFactory {

  private static final Method GET_ARGS_METHOD;

  static {
    try {
      GET_ARGS_METHOD = FlagAccessor.class.getMethod("getArguments");
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException("Unexpected exception: Can't find getArguments method", e);
    }
  }

  private static final Supplier<Object> getArgumentsSupplier =
      new Supplier<Object>() {
        @Override public Object get() {
          return Flags.getInstance().getArguments();
        }
      };

  private final Injector injector;

  @Inject FlagAccessorFactory(Injector injector) {
    this.injector = injector;
  }

  private Supplier<Object> getSupplierFor(Class<?> c, Method m) {
    Flag annotation = m.getAnnotation(Flag.class);
    Preconditions.checkState(annotation != null,
        String.format("Method %s in interface %s does not have a @Flag annotation", m, c));
    Preconditions.checkState(
        m.getParameterTypes().length == 0,
        "Flag method %s in %s must not take any parameters", m, c);
    String stringValue = Flags.getInstance().getStringRepresentationFor(annotation.name());
    if (stringValue == null) {
      Preconditions.checkState(!annotation.required(), String.format(
          "Required flag %s [%s] (from %s in %s) not set",
          annotation.name(), annotation.description(), m, c));
      stringValue = annotation.defaultValue();
      if (Strings.isNullOrEmpty(stringValue)) {
        Preconditions.checkState(!m.getReturnType().isPrimitive(),
            "Trying to convert an absent flag value into a primitive on method %s in %s", m, c);
        return Suppliers.ofInstance(null);
      }
    }

    Class<? extends FlagValueParser> valueParserClass = annotation.parser();
    FlagValueParser parser = Preconditions.checkNotNull(injector.getInstance(valueParserClass));

    Object value = parser.parseStringRepresentation(m.getGenericReturnType(), stringValue);
    return Suppliers.ofInstance(value);
  }

  private ImmutableMap<Method, Supplier<Object>> getMethodToReturnValueMap(Class<?> iface) {
    ImmutableMap.Builder<Method, Supplier<Object>> methodMapBuilder =
        ImmutableMap.<Method, Supplier<Object>>builder()
            .put(GET_ARGS_METHOD, getArgumentsSupplier);

    for (Method m : iface.getMethods()) {
      if (m.equals(GET_ARGS_METHOD)) {
        continue;
      }

      methodMapBuilder.put(m, getSupplierFor(iface, m));
    }

    return methodMapBuilder.build();
  }

  /**
   * Gets an instance of a specific flag accessor.
   */
  public <T extends FlagAccessor> T getFlagAccessor(Class<T> flagAccessorClass) {
    Preconditions.checkNotNull(flagAccessorClass);
    Preconditions.checkArgument(flagAccessorClass.isInterface(),
        "Only interfaces can be created using the getFlagAccessor method");
    Preconditions.checkArgument(!flagAccessorClass.equals(FlagAccessor.class),
        "Not permitted to create FlagAccessor instance for parent interface type");

    return Reflection.newProxy(
        flagAccessorClass,
        new FlagAccessorInvocationHandler(getMethodToReturnValueMap(flagAccessorClass)));
  }

  /**
   * Gets an instance of this factory that does the best it can without be GUICE configured.
   */
  public static FlagAccessorFactory getNonInjectedInstance() {
    return new FlagAccessorFactory(Guice.createInjector(new AbstractModule() {
      @Override protected void configure() {
        // Nothing configured.
      }
    }));
  }

  /**
   * {@link InvocationHandler} for {@link FlagAccessor} proxies.
   */
  private static class FlagAccessorInvocationHandler extends AbstractInvocationHandler {

    private final ImmutableMap<Method, Supplier<Object>> methodToReturnValueMap;

    private FlagAccessorInvocationHandler(Map<Method, Supplier<Object>> methodToReturnValueMap) {
      this.methodToReturnValueMap = ImmutableMap.copyOf(methodToReturnValueMap);
    }

    @Override protected Object handleInvocation(Object proxy, Method method, Object[] args)
        throws Throwable {
      return methodToReturnValueMap.get(method).get();
    }
  }
}
