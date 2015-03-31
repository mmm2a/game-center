package com.morgan.testing;

import com.google.common.truth.FailureStrategy;
import com.google.common.truth.Subject;
import com.google.common.truth.SubjectFactory;
import com.google.common.truth.Truth;
import com.google.gwt.safehtml.shared.SafeUri;

/**
 * {@link Subject} for {@link SafeUri} instances which have wacky default print formats.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class SafeUriSubject extends Subject<SafeUriSubject, SafeUri> {

  public static final SubjectFactory<SafeUriSubject, SafeUri> SAFE_URI_SUBJECT_FACTORY =
      new SafeUriSubjectFactory();

  public static SafeUriSubject assertThat(SafeUri subject) {
    return Truth.assertAbout(SAFE_URI_SUBJECT_FACTORY).that(subject);
  }

  SafeUriSubject(FailureStrategy failureStrategy, SafeUri subject) {
    super(failureStrategy, subject);
  }

  public void containsString(String value) {
    if (!getSubject().asString().equals(value)) {
      failWithRawMessage("Not true that [SafeUri]:%s containsString %s",
          getSubject().asString(), value);
    }
  }

  @Override public void isEqualTo(Object other) {
    if (!(other instanceof SafeUri)) {
      failWithRawMessage("Not true that [SafeUri]:%s is equal to %s",
          getSubject().asString(), other);
    } else if (!getSubject().equals(other)) {
      failWithRawMessage("Not true that [SafeUri]:%s is equal to [SafeUri]:%s",
          getSubject().asString(), ((SafeUri) other).asString());
    }
  }

  private static final class SafeUriSubjectFactory extends SubjectFactory<SafeUriSubject, SafeUri> {

    private SafeUriSubjectFactory() {
    }

    @Override public SafeUriSubject getSubject(FailureStrategy fs, SafeUri that) {
      return new SafeUriSubject(fs, that);
    }
  }
}
