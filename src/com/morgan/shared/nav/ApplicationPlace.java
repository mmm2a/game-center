package com.morgan.shared.nav;

import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Preconditions;
import com.google.gwt.place.shared.Place;

/**
 * A place type representing <i>where</i> in the application a page resides along with information
 * that feeds into that page.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public abstract class ApplicationPlace extends Place {

  private final ClientApplication clientApplication;

  protected ApplicationPlace(ClientApplication clientApplication) {
    this.clientApplication = Preconditions.checkNotNull(clientApplication);
  }

  public final ClientApplication getClientApplication() {
    return clientApplication;
  }

  protected ToStringHelper addToStringFields(ToStringHelper helper) {
    return helper.add("clientApplication", clientApplication);
  }

  @Override public int hashCode() {
    return Objects.hash(clientApplication);
  }

  @Override public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof ApplicationPlace)) {
      return false;
    }

    return clientApplication == ((ApplicationPlace) o).clientApplication;
  }

  @Override public final String toString() {
    return addToStringFields(MoreObjects.toStringHelper(getClass())).toString();
  }
}
