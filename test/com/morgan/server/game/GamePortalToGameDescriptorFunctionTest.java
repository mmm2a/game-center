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
 * Tests for the {@link GamePortalToGameDescriptorFunction} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class GamePortalToGameDescriptorFunctionTest {

  private static final GameIdentifier IDENTIFIER = new GameIdentifier("id");
  private static final String NAME = "name";
  private static final SafeHtml DESCRIPTION = SafeHtmlUtils.fromString("description");
  private static final SafeUri GAME_ICON = UriUtils.fromString("game-icon");
  private static final SafeUri DEFAULT_ICON = UriUtils.fromString("default-icon");

  @Mock private GameStaticResources mockGameStaticResources;

  @Mock private GamePortal mockGamePortal;

  private GamePortalToGameDescriptorFunction function;
  private GameDescriptor.Builder commonDescriptorBuilder;

  @Before public void createTestInstances() {
    function = new GamePortalToGameDescriptorFunction(mockGameStaticResources);

    commonDescriptorBuilder = GameDescriptor.builderFor(IDENTIFIER)
        .setName(NAME)
        .setDescription(DESCRIPTION);
  }

  @Before public void setUpCommonMockInteractions() {
    when(mockGamePortal.getGameIdentifier()).thenReturn(IDENTIFIER);
    when(mockGamePortal.getDescription()).thenReturn(DESCRIPTION);
    when(mockGamePortal.getName()).thenReturn(NAME);

    when(mockGameStaticResources.defaultGameIcon()).thenReturn(DEFAULT_ICON);
  }

  @Test public void apply_nullInput_producesNullOutput() {
    assertThat(function.apply(null)).isNull();
  }

  @Test public void apply_noGameIcon_usesDefaultIcon() {
    when(mockGamePortal.getGameIcon()).thenReturn(Optional.absent());
    assertThat(function.apply(mockGamePortal)).isEqualTo(
        commonDescriptorBuilder.setIcon(DEFAULT_ICON)
        .build());
  }

  @Test public void apply_withGameIcon_usesGameIcon() {
    when(mockGamePortal.getGameIcon()).thenReturn(Optional.of(GAME_ICON));
    assertThat(function.apply(mockGamePortal)).isEqualTo(
        commonDescriptorBuilder.setIcon(GAME_ICON)
        .build());
  }
}
