package com.morgan.server.feature;

import com.google.common.collect.ImmutableSet;
import com.morgan.server.util.flag.Flag;
import com.morgan.server.util.flag.FlagAccessor;

/**
 * A {@link FlagAccessor} used by the features module on the server.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
interface FeatureFlagAccessor extends FlagAccessor {

  @Flag(name = "disable",
      description = "A comma separated list of feature enumeration values to disable",
      defaultValue = "",
      required = false)
  ImmutableSet<String> disablements();

  @Flag(name = "enable",
      description = "A comma separated list of feature enumeration values to enable",
      defaultValue = "",
      required = false)
  ImmutableSet<String> enablements();

  @Flag(name = "features-file",
      description = "A path to a configuration file that can be read in describing the "
          + "enablement/disablement of features",
      required = false)
  String featuresFile();
}
