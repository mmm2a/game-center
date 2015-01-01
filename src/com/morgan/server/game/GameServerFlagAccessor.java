package com.morgan.server.game;

import com.morgan.server.util.flag.Flag;
import com.morgan.server.util.flag.FlagAccessor;

/**
 * Flag accessor interface for the game server engine.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
interface GameServerFlagAccessor extends FlagAccessor {

  @Flag(name = "secure",
      description = "Boolean flag indicating whether or not the server should run with HTTPS",
      required = true)
  boolean isSecure();

  @Flag(name = "port",
      description = "Integer port number to launch with",
      defaultValue = "8080",
      required = false)
  int port();
}
