package com.morgan.server.db;

import com.morgan.server.util.flag.Flag;
import com.morgan.server.util.flag.FlagAccessor;

/**
 * {@link FlagAccessor} for the db package.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public interface DbFlagAccessor extends FlagAccessor {

  @Flag(name = "persistence-unit",
      description = "String name of the persistence unit to bind",
      required = false,
      defaultValue = "gamedb")
  String persistenceUnit();
}
