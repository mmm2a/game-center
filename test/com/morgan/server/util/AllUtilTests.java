package com.morgan.server.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.morgan.server.util.cmdline.AllCommandLineTests;
import com.morgan.server.util.common.AllCommonTests;
import com.morgan.server.util.flag.AllFlagTests;
import com.morgan.server.util.log.AllLogTests;
import com.morgan.server.util.macro.AllMacroTests;
import com.morgan.server.util.mime.AllMimeTests;
import com.morgan.server.util.soy.AllSoyTests;
import com.morgan.server.util.stat.AllStatTests;

@RunWith(Suite.class)
@SuiteClasses({
  AllCommonTests.class,
  AllFlagTests.class,
  AllLogTests.class,
  AllMacroTests.class,
  AllMimeTests.class,
  AllCommandLineTests.class,
  AllSoyTests.class,
  AllStatTests.class,
  })
public class AllUtilTests {

}
