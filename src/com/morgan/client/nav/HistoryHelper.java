package com.morgan.client.nav;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;
import com.google.inject.Inject;

/**
 * Helper class for helping work with the GWT {@link History} class in a testable, injectable
 * way.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class HistoryHelper {

  @Inject HistoryHelper() {
  }
  
  /** See {@link History#addValueChangeHandler(ValueChangeHandler)} */
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
    return History.addValueChangeHandler(handler);
  }
  
  /** See {@link History#encodeHistoryToken(String)} */
  public String encodeHistoryToken(String token) {
    return History.encodeHistoryToken(token);
  }
  
  /** See {@link History#getToken()} */
  public String getToken() {
    return History.getToken();
  }
  
  /** See {@link History#back()} */
  public void back() {
    History.back();
  }
  
  /** See {@link History#forward()} */
  public void forward() {
    History.forward();
  }
  
  /** See {@link History#fireCurrentHistoryState()} */
  public void fireCurrentHistoryState() {
    History.fireCurrentHistoryState();
  }
  
  /** See {@link History#newItem(String)} */
  public void newItem(String token) {
    History.newItem(token);
  }
  
  /** See {@link History#newItem(String, boolean)} */
  public void newItem(String token, boolean fireEvent) {
    History.newItem(token, fireEvent);
  }
  
  /** See {@link History#replaceItem(String)} */
  public void replaceItem(String token) {
    History.replaceItem(token);
  }
  
  /** See {@link History#replaceItem(String, boolean)} */
  public void replaceItem(String token, boolean fireEvent) {
    History.replaceItem(token, fireEvent);
  }
}
