package com.morgan.server.constants;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.inject.Inject;
import com.google.template.soy.data.SanitizedContent;
import com.google.template.soy.data.SanitizedContent.ContentKind;
import com.google.template.soy.data.UnsafeSanitizedContentOrdainer;

/**
 * Class for a type that stores a constants map that can be written out to the client.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class PageConstants {

  private final ImmutableMap.Builder<Enum<?>, JsonElement> mapBuilder = ImmutableMap.builder();

  @Inject PageConstants() {
  }

  public <T extends Enum<T>> PageConstants add(T key, JsonElement element) {
    mapBuilder.put(Preconditions.checkNotNull(key), Preconditions.checkNotNull(element));
    return this;
  }

  public <T extends Enum<T>> PageConstants add(T key, byte n) {
    return add(key, new JsonPrimitive(n));
  }

  public <T extends Enum<T>> PageConstants add(T key, short n) {
    return add(key, new JsonPrimitive(n));
  }

  public <T extends Enum<T>> PageConstants add(T key, int n) {
    return add(key, new JsonPrimitive(n));
  }

  public <T extends Enum<T>> PageConstants add(T key, long n) {
    return add(key, new JsonPrimitive(n));
  }

  public <T extends Enum<T>> PageConstants add(T key, char c) {
    return add(key, new JsonPrimitive(c));
  }

  public <T extends Enum<T>> PageConstants add(T key, boolean b) {
    return add(key, new JsonPrimitive(b));
  }

  public <T extends Enum<T>> PageConstants add(T key, String str) {
    return add(key, new JsonPrimitive(str));
  }

  public <T extends Enum<T>> PageConstants add(T key, Object o) {
    return add(key, new Gson().toJsonTree(o));
  }

  public SanitizedContent emit() {
    JsonObject obj = new JsonObject();
    for (Map.Entry<Enum<?>, JsonElement> entry : mapBuilder.build().entrySet()) {
      obj.add(entry.getKey().name(), entry.getValue());
    }

    return UnsafeSanitizedContentOrdainer.ordainAsSafe(new Gson().toJson(obj), ContentKind.JS);
  }
}
