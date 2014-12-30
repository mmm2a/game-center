package com.morgan.shared.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.morgan.shared.util.cmdline.AllCommandLineTests;

@RunWith(Suite.class)
@SuiteClasses({
  AllCommandLineTests.class
})
public class AllUtilTests {
}
