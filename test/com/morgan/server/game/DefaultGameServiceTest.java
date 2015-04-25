package com.morgan.server.game;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableMap;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.UriUtils;
import com.morgan.server.game.modules.GamePortal;
import com.morgan.server.game.modules.GamePortals;
import com.morgan.shared.game.modules.GameDescriptor;
import com.morgan.shared.game.modules.GameIdentifier;

/**
 * Tests for the {@link DefaultGameService} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultGameServiceTest {

  private static final GameIdentifier IDENTIFIER_1 = new GameIdentifier("id1");
  private static final GameIdentifier IDENTIFIER_2 = new GameIdentifier("id2");
  private static final GameIdentifier IDENTIFIER_3 = new GameIdentifier("id3");

  private static final GameDescriptor DESCRIPTOR_1 = GameDescriptor.builderFor(IDENTIFIER_1)
      .setDescription(SafeHtmlUtils.fromString("description 1"))
      .setName("game 1")
      .setIcon(UriUtils.fromString("uri-1"))
      .build();
  private static final GameDescriptor DESCRIPTOR_2 = GameDescriptor.builderFor(IDENTIFIER_2)
      .setDescription(SafeHtmlUtils.fromString("description 2"))
      .setName("game 2")
      .setIcon(UriUtils.fromString("uri-2"))
      .build();
  private static final GameDescriptor DESCRIPTOR_3 = GameDescriptor.builderFor(IDENTIFIER_3)
      .setDescription(SafeHtmlUtils.fromString("description 3"))
      .setName("game 3")
      .setIcon(UriUtils.fromString("uri-3"))
      .build();

  @Mock private GamePortals mockGamePortals;

  @Mock private GamePortal mockGamePortal1;
  @Mock private GamePortal mockGamePortal2;
  @Mock private GamePortal mockGamePortal3;

  @Mock private GamePortalToGameDescriptorFunction mockFunction;

  private DefaultGameService service;

  @Before public void createTestInstances() {
    service = new DefaultGameService(mockGamePortals, mockFunction);
  }

  @Before public void setUpCommonMockInteractions() {
    when(mockFunction.apply(mockGamePortal1)).thenReturn(DESCRIPTOR_1);
    when(mockFunction.apply(mockGamePortal2)).thenReturn(DESCRIPTOR_2);
    when(mockFunction.apply(mockGamePortal3)).thenReturn(DESCRIPTOR_3);

    when(mockGamePortals.getPortalsMap()).thenReturn(ImmutableMap.of(
        IDENTIFIER_1, mockGamePortal1,
        IDENTIFIER_2, mockGamePortal2,
        IDENTIFIER_3, mockGamePortal3));
  }

  @Test public void getAllGames() throws Exception {
    Iterable<GameDescriptor> descriptors = service.getAllGames();
    assertThat(descriptors).hasSize(3);
    assertThat(descriptors).containsExactly(DESCRIPTOR_1, DESCRIPTOR_2, DESCRIPTOR_3);
  }
}
