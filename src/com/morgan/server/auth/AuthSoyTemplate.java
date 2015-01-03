package com.morgan.server.auth;

import com.google.template.soy.data.SanitizedContent;
import com.google.template.soy.tofu.SoyTofu.Renderer;
import com.morgan.server.util.soy.Soy;
import com.morgan.server.util.soy.SoyParameter;
import com.morgan.server.util.soy.SoyTemplate;

/**
 * {@link SoyTemplate} for the auth package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Soy(namespace = "com.morgan.server.auth",
    resource = "resources/auth.soy")
interface AuthSoyTemplate extends SoyTemplate {

  Renderer hostPage(@SoyParameter(name = "pageConstants") SanitizedContent pageConstants);
}
