package com.morgan.server.email;

import java.util.regex.Pattern;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

/**
 * A class for validating email addresses as being of the correct format.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class EmailValidator implements Predicate<String> {

  /** The one and only instance of the {@link EmailValidator} type. */
  public static final EmailValidator VALIDATOR = new EmailValidator();

  private static final Pattern EMAIL_PATTERN = Pattern.compile(
      "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);

  private EmailValidator() {
    // Do not instantiate
  }

  @Override public boolean apply(@Nullable String input) {
    if (input == null) {
      return false;
    }

    return EMAIL_PATTERN.matcher(input).matches();
  }

  /**
   * Tries to validate the input email and, if it fails, throws {@link IllegalArgumentException}.
   * Otherwise, returns the valid email address.
   */
  public String validate(@Nullable String email) {
    Preconditions.checkArgument(apply(email));
    return email;
  }
}
