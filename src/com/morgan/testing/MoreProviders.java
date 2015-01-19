package com.morgan.testing;

import java.util.Iterator;

import com.google.common.collect.Iterables;
import com.google.inject.Provider;

public class MoreProviders {
  
  @SafeVarargs
  public static <T> Provider<T> of(T... values) {
    final Iterator<T> iter = Iterables.<T>cycle(values).iterator();
    return new Provider<T>() {

      @Override public T get() {
        return iter.next();
      }
    };
  }
}
