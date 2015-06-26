package com.morgan.server.mtg.json;

import java.util.Locale;
import java.util.function.Function;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.morgan.server.util.log.AdvancedLogger;
import com.morgan.server.util.log.InjectLogger;

/**
 * Function that translates a string language in JSON into its {@link Locale}.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class JsonLanguagesFunction implements Function<String, Locale> {

  private static final ImmutableMap<String, Locale> SPECIAL_CASES = ImmutableMap.of(
      "Chinese Traditional", Locale.CHINESE,
      "Chinese Simplified", Locale.SIMPLIFIED_CHINESE,
      "Portuguese (Brazil)", Locale.forLanguageTag("pt-BR"),
      "Portuguese", Locale.forLanguageTag("pt"));

  @InjectLogger private AdvancedLogger log = AdvancedLogger.NULL;

  @Inject JsonLanguagesFunction() {
  }

  @Override public Locale apply(String t) {
    Locale l = SPECIAL_CASES.get(t);
    if (l == null) {
        l = Preconditions.checkNotNull(Locale.forLanguageTag(t), "For language code " + t);
    }
    if (l.toString().isEmpty()) {
      log.error("Got empty locale for '%s'", t);
    }
    return l;
  }
}
