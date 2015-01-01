package com.morgan.server.security;

import com.google.common.base.Function;

/**
 * An interface for a type that can obfuscate (and de-obfuscate) values of a given type.
 *
 * @param <T> the input value type that gets obfuscated.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface ValueObfuscation<T> {

  Function<T, String> getObfuscationFunction();
  Function<String, T> getDeobfuscationFunction();
}
