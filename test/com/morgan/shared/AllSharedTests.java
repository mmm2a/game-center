package com.morgan.shared;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.morgan.shared.nav.AllNavTests;
import com.morgan.shared.util.AllUtilTests;
import com.morgan.shared.util.macro.AllMacroTests;

@RunWith(Suite.class)
@SuiteClasses({
  AllMacroTests.class,
  AllNavTests.class,
  AllUtilTests.class
})
public class AllSharedTests {

}
