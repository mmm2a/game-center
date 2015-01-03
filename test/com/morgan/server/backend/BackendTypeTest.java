package com.morgan.server.backend;

import org.junit.Test;

import com.google.common.truth.Truth;
import com.morgan.server.util.cmdline.CommandLine;
import com.morgan.server.util.flag.Flags;

/**
 * Tests for the {@link BackendType} enumeration.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class BackendTypeTest {

  @Test public void getBackendType() {
    for (BackendType type : BackendType.values()) {
      Flags.overrideWith(
          CommandLine.builder().addFlag("backend-type", type.name()).build());
      Truth.assertThat(BackendType.getCurrent()).isEqualTo(type);
    }
  }
}
