package com.morgan.server.util.soy.fake;

import java.lang.reflect.Method;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.Reflection;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.template.soy.data.SanitizedContent;
import com.google.template.soy.data.SanitizedContent.ContentKind;
import com.google.template.soy.data.UnsafeSanitizedContentOrdainer;
import com.google.template.soy.tofu.SoyTofu.Renderer;
import com.morgan.server.util.soy.SoyTemplate;

/**
 * A factory class for creating fake {@link SoyTemplate} instances.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class FakeSoyTemplateFactory {

  public static <T extends SoyTemplate> T createProxyWithRendererFor(
      @Nullable Renderer renderer, Class<T> soyTemplate) {
    return Reflection.newProxy(soyTemplate, new FakeSoyTemplateInvocationHandler(renderer));
  }

  public static <T extends SoyTemplate> T createProxyFor(Class<T> soyTemplate) {
    return createProxyWithRendererFor(null, soyTemplate);
  }

  private static final class FakeSoyTemplateInvocationHandler extends AbstractInvocationHandler {

    @Nullable private final Renderer renderer;

    FakeSoyTemplateInvocationHandler(@Nullable Renderer renderer) {
      this.renderer = renderer;
    }

    @Override protected Object handleInvocation(
        Object proxy, Method method, Object[] args) throws Throwable {

      StringBuilder builder = new StringBuilder(method.getName());
      builder.append("[");
      for (int i = 0; i < args.length; i++) {
        if (i > 0) {
          builder.append(", ");
        }
        builder.append("" + args[i]);
      }
      builder.append("]");

      String rawOutput = builder.toString();

      Class<?> mRetT = method.getReturnType();
      if (mRetT.isAssignableFrom(String.class)) {
        return rawOutput;
      } else if (mRetT.isAssignableFrom(SanitizedContent.class)) {
        return UnsafeSanitizedContentOrdainer.ordainAsSafe(rawOutput, ContentKind.TEXT);
      } else if (mRetT.isAssignableFrom(Renderer.class)) {
        return Preconditions.checkNotNull(renderer);
      } else if (mRetT.isAssignableFrom(SafeHtml.class)) {
        return SafeHtmlUtils.fromString(rawOutput);
      } else {
        throw new IllegalStateException(String.format(
                "Unable to create final renderer for return type of soy method %s", method));
      }
    }
  }
}
