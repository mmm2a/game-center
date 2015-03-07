package com.morgan.client.game;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Entry point for the game web app.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class GameAppEntryPoint implements EntryPoint {

  private final GameAppGinjector injector = GWT.create(GameAppGinjector.class);

  @Override public void onModuleLoad() {
    injector.getGameApplication().startApplication();
  }
}
