package com.morgan.client.page;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.IsWidget;
import com.morgan.shared.nav.ApplicationPlace;

/**
 * An interface for a type that can present a page onto the client screen.
 *
 * @param <P> The application place that this page renders.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface PagePresenter<P extends ApplicationPlace> {
  /**
   * Asks this presenter to present the correct page for the given place.
   */
  Optional<? extends IsWidget> presentPageFor(P place);
}
