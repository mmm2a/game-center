package com.morgan.shared;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.morgan.shared.util.AllUtilTests;

@RunWith(Suite.class)
@SuiteClasses({
  AllUtilTests.class
})
public class AllSharedTests {

}
