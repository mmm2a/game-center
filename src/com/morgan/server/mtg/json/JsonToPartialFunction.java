package com.morgan.server.mtg.json;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Partial;
import org.joda.time.ReadablePartial;
import org.joda.time.YearMonth;

import com.google.inject.Inject;

/**
 * Converts the JSON representation of a partial date (year, year-month, or year-month-day) to a
 * {@link Partial}.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class JsonToPartialFunction implements Function<String, ReadablePartial> {

  private static final Pattern YEAR_MONTH_PARTIAL = Pattern.compile("(\\d{4})-(\\d{2})");
  private static final Pattern YEAR_MONTH_DAY_PARTIAL =
      Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");

  @Inject JsonToPartialFunction() {
  }

  @Override public ReadablePartial apply(String t) {
    Matcher matcher = YEAR_MONTH_DAY_PARTIAL.matcher(t);
    if (matcher.matches()) {
      return new LocalDate(
          Integer.parseInt(matcher.group(1)),
          Integer.parseInt(matcher.group(2)),
          Integer.parseInt(matcher.group(3)));
    }

    matcher = YEAR_MONTH_PARTIAL.matcher(t);
    if (matcher.matches()) {
      return new YearMonth(
          Integer.parseInt(matcher.group(1)),
          Integer.parseInt(matcher.group(2)));
    }

    return new Partial(DateTimeFieldType.year(), Integer.parseInt(t));
  }
}
