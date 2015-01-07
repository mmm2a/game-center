package com.morgan.server.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.morgan.server.util.cmdline.AllCommandLineTests;
import com.morgan.server.util.common.AllCommonTests;
import com.morgan.server.util.flag.AllFlagTests;
import com.morgan.server.util.log.AllLogTests;
import com.morgan.server.util.soy.AllSoyTests;

@RunWith(Suite.class)
@SuiteClasses({
  AllCommonTests.class,
  AllFlagTests.class,
  AllLogTests.class,
  AllCommandLineTests.class,
  AllSoyTests.class,
  })
public class AllUtilTests {

}
