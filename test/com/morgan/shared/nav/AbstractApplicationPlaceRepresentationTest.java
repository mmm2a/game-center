package com.morgan.shared.nav;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Tests for the {@link AbstractApplicationPlaceRepresentation} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractApplicationPlaceRepresentationTest {

  @Mock private ApplicationPlace mockPlace;

  @Spy private TestableAbstractApplicationPlaceRepresentation spyRep;

  @Test public void parsePlaceFromToken_doesNotStartWithTokenPrefix_returnsNull() {
    assertThat(spyRep.parsePlaceFromToken("something")).isNull();
  }

  @Test public void parsePlaceFromToken_startsWithParens_returnsNull() {
    assertThat(spyRep.parsePlaceFromToken("!(something)")).isNull();
  }

  @Test public void parsePlaceFromToken_onlyOnePart() {
    doReturn(mockPlace).when(spyRep)
        .parseFromParts(ImmutableList.of("one"), ImmutableMap.<String, String>of());
    assertThat(spyRep.parsePlaceFromToken("!one")).isEqualTo(mockPlace);
    assertThat(spyRep.parsePlaceFromToken("!/one")).isEqualTo(mockPlace);
    assertThat(spyRep.parsePlaceFromToken("!one/")).isEqualTo(mockPlace);
    assertThat(spyRep.parsePlaceFromToken("!//one/")).isEqualTo(mockPlace);
  }

  @Test public void parsePlaceFromToken_multipleParts() {
    doReturn(mockPlace).when(spyRep)
        .parseFromParts(ImmutableList.of("one", "two", "three"),
                ImmutableMap.<String, String>of());
    assertThat(spyRep.parsePlaceFromToken("!one/two/three")).isEqualTo(mockPlace);
    assertThat(spyRep.parsePlaceFromToken("!/one//two///three/")).isEqualTo(mockPlace);
  }

  @Test public void parsePlaceFromToken_withOpenParensButNotClose_returnsNull() {
    assertThat(spyRep.parsePlaceFromToken("!one(something")).isNull();
  }

  @Test public void parsePlaceFromToken_withEmptyParams() {
    doReturn(mockPlace).when(spyRep)
        .parseFromParts(ImmutableList.of("one"), ImmutableMap.<String, String>of());
    assertThat(spyRep.parsePlaceFromToken("!one()")).isEqualTo(mockPlace);
  }

  @Test public void parsePlaceFromToken_withOneParam() {
    doReturn(mockPlace).when(spyRep)
        .parseFromParts(ImmutableList.of("one"),
            ImmutableMap.<String, String>of("alpha", "1"));
    assertThat(spyRep.parsePlaceFromToken("!one(alpha=1)")).isEqualTo(mockPlace);
  }

  @Test public void parsePlaceFromToken_withMultipleParams() {
    doReturn(mockPlace).when(spyRep)
        .parseFromParts(ImmutableList.of("one"),
            ImmutableMap.<String, String>of("alpha", "1", "beta", "2", "gamma", "3"));
    assertThat(spyRep.parsePlaceFromToken("!one(alpha=1,beta=2,gamma=3)"))
        .isEqualTo(mockPlace);
  }

  @Test public void parsePlaceFromToken_complex() {
    doReturn(mockPlace).when(spyRep)
        .parseFromParts(ImmutableList.of("one", "two", "three"),
            ImmutableMap.<String, String>of("alpha", "1", "beta", "2", "gamma", "3"));
    assertThat(spyRep.parsePlaceFromToken("!one/two/three(alpha=1,beta=2,gamma=3)"))
        .isEqualTo(mockPlace);
  }

  @Test public void parsePlaceFromToken_badParameters() {
    doReturn(mockPlace).when(spyRep)
        .parseFromParts(ImmutableList.of("one", "two", "three"),
            ImmutableMap.<String, String>of("alpha", "1", "beta", "2", "gamma", "3"));
    assertThat(spyRep.parsePlaceFromToken("!one/two/three(alpha=1,beta,gamma=3)"))
        .isNull();
  }

  @Test public void generateUrlTokenFor_noParameters_singlePath() {
    when(mockPlace.getParameters()).thenReturn(ImmutableMap.<String, String>of());
    doReturn(ImmutableList.of("one")).when(spyRep).getPathPartsFor(mockPlace);
    assertThat(spyRep.generateUrlTokenFor(mockPlace)).isEqualTo("!one");
  }

  @Test public void generateUrlTokenFor_noParameters_multiPath() {
    when(mockPlace.getParameters()).thenReturn(ImmutableMap.<String, String>of());
    doReturn(ImmutableList.of("one", "two", "three"))
        .when(spyRep).getPathPartsFor(mockPlace);
    assertThat(spyRep.generateUrlTokenFor(mockPlace)).isEqualTo("!one/two/three");
  }

  @Test public void generateUrlTokenFor_oneParameter() {
    when(mockPlace.getParameters()).thenReturn(ImmutableMap.of("alpha", "1"));
    doReturn(ImmutableList.of("one", "two", "three"))
        .when(spyRep).getPathPartsFor(mockPlace);
    assertThat(spyRep.generateUrlTokenFor(mockPlace)).isEqualTo("!one/two/three(alpha=1)");
  }

  @Test public void generateUrlTokenFor_multiParameter() {
    when(mockPlace.getParameters())
        .thenReturn(ImmutableMap.of("alpha", "1", "beta", "2", "gamma", "3"));
    doReturn(ImmutableList.of("one", "two", "three"))
        .when(spyRep).getPathPartsFor(mockPlace);
    assertThat(spyRep.generateUrlTokenFor(mockPlace))
        .isEqualTo("!one/two/three(alpha=1,beta=2,gamma=3)");
  }

  static class TestableAbstractApplicationPlaceRepresentation
      extends AbstractApplicationPlaceRepresentation {

    @Override protected ApplicationPlace parseFromParts(ImmutableList<String> pathParts,
        ImmutableMap<String, String> parameterMap) {
      throw new UnsupportedOperationException();
    }

    @Override protected List<String> getPathPartsFor(ApplicationPlace place) {
      throw new UnsupportedOperationException();
    }
  }
}
