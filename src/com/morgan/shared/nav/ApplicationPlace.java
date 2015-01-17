package com.morgan.shared.nav;

import java.util.Map;
import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.gwt.place.shared.Place;

/**
 * A place type representing <i>where</i> in the application a page resides along with information
 * that feeds into that page.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public abstract class ApplicationPlace extends Place {

  private final ClientApplication clientApplication;
  private final ImmutableMap<String, String> parameters;

  protected ApplicationPlace(ClientApplication clientApplication, Map<String, String> parameters) {
    this.clientApplication = Preconditions.checkNotNull(clientApplication);
    this.parameters = ImmutableMap.copyOf(parameters);
  }

  protected ApplicationPlace(ClientApplication clientApplication) {
    this(clientApplication, ImmutableMap.<String, String>of());
  }

  final ClientApplication getClientApplication() {
    return clientApplication;
  }

  ImmutableMap<String, String> getParameters() {
    return parameters;
  }

  protected ToStringHelper addToStringFields(ToStringHelper helper) {
    return helper.add("clientApplication", clientApplication)
        .add("parameters", parameters);
  }

  /**
   * Gets a {@link ApplicationPlaceRepresentation} that can represent this place type.
   */
  public abstract ApplicationPlaceRepresentation getRepresentation();

  @Override public int hashCode() {
    return Objects.hash(clientApplication, parameters);
  }

  @Override public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof ApplicationPlace)) {
      return false;
    }

    return clientApplication == ((ApplicationPlace) o).clientApplication
        && parameters.equals(((ApplicationPlace) o).parameters);
  }

  @Override public final String toString() {
    return addToStringFields(MoreObjects.toStringHelper(getClass())).toString();
  }
}
