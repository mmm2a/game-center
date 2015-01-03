package com.morgan.server.game;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.morgan.server.util.cmdline.CommandLine;
import com.morgan.server.util.cmdline.CommandLineParser;
import com.morgan.server.util.flag.Flags;

/**
 * Main application launcher for the game server.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class GameServerLauncher {

  public static void main(String[] args) throws Exception {
    // Initialize the flags global
    CommandLineParser parser = new CommandLineParser(args);
    Flags.initializeWith(parser.supplyCommandLineContents(CommandLine.builder()).build());

    // Then, create a simple GUICE injector to bootstrap the server, then the server will likewise
    // create one of its own for the rest of the application.
    Injector injector = Guice.createInjector(new BootstrapModule());
    GameServer server = injector.getInstance(GameServer.class);
    server.start();
  }
}
