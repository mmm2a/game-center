package com.morgan.server.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.morgan.server.util.cmdline.AllCommandLineTests;
import com.morgan.server.util.flag.AllFlagTests;

@RunWith(Suite.class)
@SuiteClasses({
  AllFlagTests.class,
  AllCommandLineTests.class,
  })
public class AllUtilTests {

}