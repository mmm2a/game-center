package com.morgan.shared;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.morgan.shared.auth.AllAuthTests;
import com.morgan.shared.nav.AllNavTests;

@RunWith(Suite.class)
@SuiteClasses({
  AllAuthTests.class,
  AllNavTests.class,
})
public class AllSharedTests {

}
