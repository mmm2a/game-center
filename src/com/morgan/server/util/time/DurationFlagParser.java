package com.morgan.server.util.time;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.Duration;

import com.google.common.base.Preconditions;
import com.morgan.server.util.flag.FlagValueParser;

/**
 * A {@link FlagValueParser} that can be used to parse durations.  Durations are parsed using the
 * following pattern:
 * <code> <number> <unit> </code>
 * where spaces are ignored between the number and the unit.  Valid units are:
 * <ul>
 *   <li>s - seconds
 *   <li>m - minutes
 *   <li>h - hours
 *   <li>d - days
 * </ul>
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class DurationFlagParser implements FlagValueParser {

  private static final Pattern DURATION_PATTERN = Pattern.compile(
      "^\\s*(\\d+)\\s*(s|m|h|d)\\s*$");

  @Override public Object parseStringRepresentation(Type valueType, String representation) {
    Preconditions.checkArgument(valueType instanceof Class);
    Preconditions.checkArgument(((Class<?>) valueType).isAssignableFrom(Duration.class));

    Matcher matcher = DURATION_PATTERN.matcher(representation);
    Preconditions.checkState(matcher.matches());

    int num = Integer.parseInt(matcher.group(1));
    String unit = matcher.group(2);

    switch (unit) {
      case "s" :
        return Duration.standardSeconds(num);

      case "m" :
        return Duration.standardMinutes(num);

      case "h" :
        return Duration.standardHours(num);

      case "d" :
        return Duration.standardDays(num);
    }

    throw new IllegalStateException("Unexpected duration flag representation: " + representation);
  }
}
