package com.morgan.shared.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.morgan.shared.util.macro.AllMacroTests;

@RunWith(Suite.class)
@SuiteClasses({
  AllMacroTests.class
})
public class AllUtilTests {
}
