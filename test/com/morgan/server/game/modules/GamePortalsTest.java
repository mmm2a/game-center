package com.morgan.server.game.modules;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.morgan.shared.game.modules.GameIdentifier;

/**
 * Tests for the {@link GamePortals} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class GamePortalsTest {

  private static final GameIdentifier GAME_ID_1 = new GameIdentifier("game-1");
  private static final GameIdentifier GAME_ID_2 = new GameIdentifier("game-2");
  private static final GameIdentifier GAME_ID_3 = new GameIdentifier("game-3");

  @Mock private Injector mockInjector;

  @Mock private GamePortal mockPortal1;
  @Mock private GamePortal mockPortal2;
  @Mock private GamePortal mockPortal3;

  private GamePortals portals;

  @Before public void createTestInstances() {
    when(mockInjector.getInstance(Key.get(GamePortal.class, Names.named("game-1"))))
        .thenReturn(mockPortal1);
    when(mockInjector.getInstance(Key.get(GamePortal.class, Names.named("game-2"))))
        .thenReturn(mockPortal2);
    when(mockInjector.getInstance(Key.get(GamePortal.class, Names.named("game-3"))))
        .thenReturn(mockPortal3);

    portals = new GamePortals(ImmutableSet.of(GAME_ID_1, GAME_ID_2, GAME_ID_3), mockInjector);
  }

  @Test public void getPortalsMap() {
    assertThat(portals.getPortalsMap())
        .isEqualTo(ImmutableMap.<GameIdentifier, GamePortal>builder()
            .put(GAME_ID_1, mockPortal1)
            .put(GAME_ID_2, mockPortal2)
            .put(GAME_ID_3, mockPortal3)
            .build());
  }

  @Test public void getPortal() {
    assertThat(portals.getPortal(GAME_ID_1)).isEqualTo(mockPortal1);
    assertThat(portals.getPortal(GAME_ID_2)).isEqualTo(mockPortal2);
    assertThat(portals.getPortal(GAME_ID_3)).isEqualTo(mockPortal3);
  }
}
