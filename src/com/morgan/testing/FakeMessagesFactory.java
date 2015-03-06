package com.morgan.testing;

import java.lang.reflect.Method;

import com.google.common.base.Joiner;
import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.Reflection;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * Factory for creating fake {@link Messages} instances.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class FakeMessagesFactory {

  private static final Joiner PARAM_JOINER = Joiner.on(", ");
  
  private FakeMessagesFactory() {
    // Do not instantiate
  }
  
  public static <T extends Messages> T create(Class<T> messagesInterface) {
    return Reflection.newProxy(messagesInterface, HANDLER);
  }
  
  private static final AbstractInvocationHandler HANDLER =
      new AbstractInvocationHandler() {
        @Override protected Object handleInvocation(
            Object proxy, Method method, Object[] args) throws Throwable {
          StringBuilder builder = new StringBuilder(method.getName() + "(");
          builder.append(PARAM_JOINER.join(args));
          builder.append(")");
          String str = builder.toString();
          
          if (method.getReturnType().equals(SafeHtml.class)) {
            return SafeHtmlUtils.fromString(str);
          } else if (method.getReturnType().equals(String.class)) {
            return str;
          }
          
          throw new UnsupportedOperationException(String.format(
              "Return type %s not supported by FakeMessagesFactory",
              method.getReturnType()));
        }
      };
}
