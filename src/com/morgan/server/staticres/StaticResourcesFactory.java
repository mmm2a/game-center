package com.morgan.server.staticres;

/**
 * Factory for creating static resources interfaces.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface StaticResourcesFactory {

  <T extends StaticResources> T createProxy(Class<T> resourcesInterface);
}
