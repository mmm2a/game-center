package com.morgan.server.util.stat;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * An enumeration describing various parameters that can be added to an image URL for a stat
 * chart.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
enum StatChartParam {
  ACTION("action"),
  CONTEXT("context"),
  HEIGHT("height"),
  WIDTH("width");

  private final String paramName;

  private StatChartParam(String paramName) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(paramName));
    this.paramName = paramName;
  }

  String getParameterName() {
    return paramName;
  }
}
