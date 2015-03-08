package com.morgan.server.game;

import com.morgan.server.util.flag.Flag;
import com.morgan.server.util.flag.FlagAccessor;

/**
 * Flag accessor interface for the game server engine.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface GameServerFlagAccessor extends FlagAccessor {

  @Flag(name = "server-title",
      description = "Title for this running server",
      required = false,
      defaultValue = "Morgan Game Server")
  String serverTitle();

  @Flag(name = "secure",
      description = "Boolean flag indicating whether or not the server should run with HTTPS",
      required = true)
  boolean isSecure();

  @Flag(name = "port",
      description = "Integer port number to launch with",
      defaultValue = "8080",
      required = false)
  int port();

  @Flag(name = "war-context-path",
      description = "The context path to load the war file or resources at in the server",
      required = false,
      defaultValue = "/")
  String warContextPath();

  @Flag(name = "war-file",
      description = "Path to the war file to load for the application",
      required = false)
  String warFile();

  @Flag(name = "war-resource-base",
      description = "The path to the base of the war resource directory",
      required = false,
      defaultValue = "./war")
  String warResourceBase();

  @Flag(name = "war-descriptor-path",
      description =
          "Path where the web.xml file is found for the war-resource-base version of a web app.",
      required = false,
      defaultValue = "./war/WEB-INF/web.xml")
  String warDescriptorPath();

  @Flag(name = "war-parent-loader-priority",
      description =
          "Boolean indicating whether the parent loader has priority in the web context handler",
      required = false,
      defaultValue = "true")
  boolean isParentLoaderPriority();
}
