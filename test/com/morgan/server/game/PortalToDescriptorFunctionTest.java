package com.morgan.server.game;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Optional;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.morgan.server.game.modules.GamePortal;
import com.morgan.shared.game.modules.GameDescriptor;
import com.morgan.shared.game.modules.GameIdentifier;

/**
 * Tests for the {@link PortalToDescriptorFunction} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class PortalToDescriptorFunctionTest {

  private static final GameIdentifier ID = new GameIdentifier("game-identifier");

  private static final String NAME = "The Game";
  private static final SafeHtml DESCRIPTION = SafeHtmlUtils.fromString("The game's description");

  private static final SafeUri DEFAULT_GAME_ICON = UriUtils.fromString("default/game/icon");
  private static final SafeUri NON_DEFAULT_GAME_ICON = UriUtils.fromString("non/default/game/icon");

  @Mock private GameStaticResources mockResources;

  @Mock private GamePortal mockPortal;

  private PortalToDescriptorFunction function;

  @Before public void createTestInstances() {
    function = new PortalToDescriptorFunction(mockResources);
  }

  @Before public void setUpCommonMockInteractions() {
    when(mockResources.defaultGameIcon()).thenReturn(DEFAULT_GAME_ICON);

    when(mockPortal.getGameIdentifier()).thenReturn(ID);
    when(mockPortal.getName()).thenReturn(NAME);
    when(mockPortal.getDescription()).thenReturn(DESCRIPTION);
    when(mockPortal.getGameIcon()).thenReturn(Optional.<SafeUri>absent());
  }

  @Test public void apply_nullInput_producesNullOutput() {
    assertThat(function.apply(null)).isNull();
  }

  @Test public void apply_noIcon_usesDefault() {
    assertThat(function.apply(mockPortal)).isEqualTo(
        GameDescriptor.builderFor(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setIcon(DEFAULT_GAME_ICON)
            .build());
  }

  @Test public void apply_withIcon_usesGamesIcon() {
    when(mockPortal.getGameIcon()).thenReturn(Optional.of(NON_DEFAULT_GAME_ICON));

    assertThat(function.apply(mockPortal)).isEqualTo(
        GameDescriptor.builderFor(ID)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setIcon(NON_DEFAULT_GAME_ICON)
            .build());
  }
}
