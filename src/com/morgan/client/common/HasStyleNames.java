package com.morgan.client.common;

/**
 * Interface representing the ability for a widget to have its style altered.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface HasStyleNames {

  /** See {@link com.google.gwt.user.client.ui.UIObject#addStyleName(String)} */
  void addStyleName(String styleName);

  /** See {@link com.google.gwt.user.client.ui.UIObject#removeStyleName(String)} */
  void removeStyleName(String styleName);

  /** See {@link com.google.gwt.user.client.ui.UIObject#setStyleName(String)} */
  void setStyleName(String styleName);

  /** See {@link com.google.gwt.user.client.ui.UIObject#setStyleName(String, boolean)} */
  void setStyleName(String styleName, boolean add);
}
