package com.morgan.client.game.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.morgan.client.game.home.AllGamesWidget.GameWidget;
import com.morgan.client.mwt.a11y.AccessibleFocusPanel;

/**
 * Default implementation of the {@link GameWidget} interface.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class DefaultGameWidget extends Composite implements GameWidget {

  @UiTemplate("resources/GameWidget.ui.xml")
  interface Binder extends UiBinder<Widget, DefaultGameWidget> {
  }

  private static final Binder binder = GWT.create(Binder.class);

  @UiField AccessibleFocusPanel focusPanel;
  @UiField ImageElement icon;
  @UiField DivElement name;
  @UiField DivElement description;

  @Inject DefaultGameWidget() {
    initWidget(binder.createAndBindUi(this));
  }

  @Override public String getText() {
    return name.getInnerText();
  }

  @Override public void setText(String name) {
    this.name.setInnerText(name);
  }

  @Override public void setHTML(SafeHtml description) {
    this.description.setInnerSafeHtml(description);
  }

  @Override public HandlerRegistration addSelectionHandler(SelectionHandler<Widget> handler) {
    return focusPanel.addSelectionHandler(handler);
  }

  @Override public void setIcon(SafeUri gameIcon) {
    icon.setSrc(gameIcon.asString());
  }
}
