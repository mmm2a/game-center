package com.morgan.client.mwt.a11y;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * A type of {@link FocusPanel} that raises a {@link SelectionEvent} when the panel is either
 * clicked or selected with the enter key or space key.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class AccessibleFocusPanel
    extends FocusPanel implements HasEnabled, HasSelectionHandlers<Widget> {

  private final ClickHandler clickHandler = new ClickHandler() {
    @Override public void onClick(ClickEvent event) {
      handleClick();
    }
  };

  private final KeyDownHandler keyDownHandler = new KeyDownHandler() {
    @Override public void onKeyDown(KeyDownEvent event) {
      handleKeyDown(event.getNativeKeyCode());
    }
  };

  private boolean isEnabled = true;

  public AccessibleFocusPanel() {
    super();
    wireUpHandlers();
  }

  public AccessibleFocusPanel(IsWidget child) {
    this(child.asWidget());
  }

  public AccessibleFocusPanel(Widget child) {
    super(child);
    wireUpHandlers();
  }

  private void handleClick() {
    fireSelectionEvent();
  }

  private void handleKeyDown(int keyCode) {
    switch(keyCode) {
      case KeyCodes.KEY_SPACE :
      case KeyCodes.KEY_ENTER :
        fireSelectionEvent();
        break;

      default :
        // Do nothing
    }
  }

  private void wireUpHandlers() {
    addKeyDownHandler(keyDownHandler);
    addClickHandler(clickHandler);
  }

  private void fireSelectionEvent() {
    if (isEnabled) {
      SelectionEvent.fire(this, this);
    }
  }

  @Override public HandlerRegistration addSelectionHandler(SelectionHandler<Widget> handler) {
    return addHandler(handler, SelectionEvent.getType());
  }

  @Override public boolean isEnabled() {
    return isEnabled;
  }

  @Override public void setEnabled(boolean isEnabled) {
    this.isEnabled = isEnabled;
  }
}
