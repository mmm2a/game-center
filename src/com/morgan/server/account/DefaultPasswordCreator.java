package com.morgan.server.account;

import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

/**
 * Default implementation of the {@link PasswordCreator} interface.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class DefaultPasswordCreator implements PasswordCreator {

  private static final int PASSWORD_LENGTH = 8;

  private static final String ADDITIONAL_VALID_CHARS = "!@#$%^&*()+=-?;:'\"<>";
  private static final ImmutableList<Character> VALID_CHARACTERS;

  static {
    ImmutableList.Builder<Character> builder = ImmutableList.builder();
    for (char i = 'a'; i <= 'z'; i++) {
      builder.add(i);
    }
    for (char i = 'A'; i <= 'Z'; i++) {
      builder.add(i);
    }
    for (char i = '0'; i <= '9'; i++) {
      builder.add(i);
    }
    for (char i : ADDITIONAL_VALID_CHARS.toCharArray()) {
      builder.add(i);
    }
    VALID_CHARACTERS = builder.build();
  }

  private final Random random;

  @Inject DefaultPasswordCreator(Random random) {
    this.random = random;
  }

  @Override public String get() {
    StringBuilder builder = new StringBuilder(PASSWORD_LENGTH);
    for (int i = 0; i < PASSWORD_LENGTH; i++) {
      builder.append(VALID_CHARACTERS.get(random.nextInt(VALID_CHARACTERS.size())));
    }
    return builder.toString();
  }
}
