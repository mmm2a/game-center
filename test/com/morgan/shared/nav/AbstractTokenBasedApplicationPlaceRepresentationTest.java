package com.morgan.shared.nav;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Tests for the {@Link AbstractTokenBasedApplicationPlaceRepresentation} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractTokenBasedApplicationPlaceRepresentationTest {

  private static final String TOKEN = "token";

  private static final ImmutableList<String> EXPECTED_PATH_PARTS =
      ImmutableList.of("one", "two", "three");
  private static final ImmutableList<String> RESULTANT_PATH_PARTS =
      ImmutableList.of("four", "five", "six");
  private static final ImmutableMap<String, String> EXPECTED_PARAMTERS =
      ImmutableMap.of("alpha", "1");

  @Mock private ApplicationPlace mockPlace;

  private TestableAbstractTokenBasedApplicationPlaceRepresentation representation;

  @Before public void createTestInstances() {
    representation = new TestableAbstractTokenBasedApplicationPlaceRepresentation();
  }

  @Test public void parseFromParts_emptyParts_returnsNull() {
    assertThat(representation.parseFromParts(ImmutableList.<String>of(), EXPECTED_PARAMTERS))
        .isNull();
  }

  @Test public void parseFromParts_doesNotStartWithToken_returnsNull() {
    assertThat(representation.parseFromParts(
        ImmutableList.<String>builder().add("not token")
            .addAll(EXPECTED_PATH_PARTS)
            .build(), EXPECTED_PARAMTERS))
        .isNull();
  }

  @Test public void parseFromParts_startsWithToken_returnsPlace() {
    assertThat(representation.parseFromParts(
        ImmutableList.<String>builder().add(TOKEN)
            .addAll(EXPECTED_PATH_PARTS)
            .build(), EXPECTED_PARAMTERS))
        .isEqualTo(mockPlace);
  }

  @Test public void getPathPartsFor_addsToken() {
    assertThat(representation.getPathPartsFor(mockPlace))
        .containsExactlyElementsIn(ImmutableList.<String>builder()
            .add(TOKEN)
            .addAll(RESULTANT_PATH_PARTS)
            .build())
        .inOrder();
  }

  private class TestableAbstractTokenBasedApplicationPlaceRepresentation
      extends AbstractTokenBasedApplicationPlaceRepresentation {

    TestableAbstractTokenBasedApplicationPlaceRepresentation() {
      super(TOKEN);
    }

    @Override protected ApplicationPlace parseFromPartsAfterToken(
        ImmutableList<String> remainingParts,
        ImmutableMap<String, String> parameterMap) {
      assertThat(remainingParts).containsExactlyElementsIn(EXPECTED_PATH_PARTS).inOrder();
      assertThat(parameterMap).isEqualTo(EXPECTED_PARAMTERS);
      return mockPlace;
    }

    @Override protected Iterable<String> getPathPartsAfterTokenFor(ApplicationPlace place) {
      assertThat(place).isEqualTo(mockPlace);
      return RESULTANT_PATH_PARTS;
    }
  }
}
