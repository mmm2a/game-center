package com.morgan.server.game;

import com.google.template.soy.data.SanitizedContent;
import com.google.template.soy.tofu.SoyTofu.Renderer;
import com.morgan.server.util.soy.Soy;
import com.morgan.server.util.soy.SoyParameter;
import com.morgan.server.util.soy.SoyTemplate;

/**
 * Soy template interface for rendering the game application host page.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Soy(namespace = "com.morgan.server.game",
    resource = "resources/game.soy")
interface GameSoyTemplate extends SoyTemplate {
  Renderer hostPage(@SoyParameter(name = "pageConstants") SanitizedContent pageConstants);
}
