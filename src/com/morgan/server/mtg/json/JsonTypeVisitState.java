package com.morgan.server.mtg.json;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
class JsonTypeVisitState {

  private final Map<String, Map<String, Long>> jsonTypeToFieldNameToUseCount =
      new HashMap<>();

  @Inject JsonTypeVisitState() {
  }

  private Map<String, Long> getFieldNameToVisitCountFor(String jsonType) {
    Map<String, Long> result = jsonTypeToFieldNameToUseCount.get(jsonType);
    if (result == null) {
      result = new HashMap<>();
      jsonTypeToFieldNameToUseCount.put(jsonType, result);
    }
    return result;
  }

  Function<String, JsonElement> getAccessorFor(String jsonType, JsonObject value) {
    Map<String, Long> fieldNameToVisitCount = getFieldNameToVisitCountFor(jsonType);
    value.entrySet().stream()
        .map(e -> e.getKey())
        .filter(k -> !fieldNameToVisitCount.containsKey(k))
        .forEach(k -> fieldNameToVisitCount.put(k,  0L));
    return new Function<String, JsonElement>() {
      @Override public JsonElement apply(String t) {
        Long l = fieldNameToVisitCount.get(t);
        l = l + 1;
        fieldNameToVisitCount.put(t, l);
        return value.get(t);
      }
    };
  }

  String generateReport() {
    StringWriter writer = new StringWriter();
    PrintWriter pWriter = new PrintWriter(writer);

    jsonTypeToFieldNameToUseCount.forEach(
        (k, v) -> {
          pWriter.format("Report for \"%s\":\n", k);
          v.forEach(
              (kk, vv) -> pWriter.format("\t%s = %d\n", kk, vv));
        });

    pWriter.close();
    return writer.toString();
  }
}
