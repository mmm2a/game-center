package com.morgan.shared;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.morgan.server.util.macro.AllMacroTests;
import com.morgan.shared.nav.AllNavTests;

@RunWith(Suite.class)
@SuiteClasses({
  AllMacroTests.class,
  AllNavTests.class,
})
public class AllSharedTests {

}
