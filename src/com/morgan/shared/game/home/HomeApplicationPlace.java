package com.morgan.shared.game.home;

import com.morgan.shared.game.BaseGameApplicationPlace;
import com.morgan.shared.nav.ApplicationPlace;
import com.morgan.shared.nav.ApplicationPlaceRepresentation;

/**
 * {@link ApplicationPlace} for the main game dashboard.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class HomeApplicationPlace extends BaseGameApplicationPlace {

  public HomeApplicationPlace() {
  }

  @Override public boolean equals(Object o) {
    return super.equals(o) && (o instanceof HomeApplicationPlace);
  }

  @Override public ApplicationPlaceRepresentation getRepresentation() {
    return new HomeApplicationPlaceRepresentation();
  }
}
