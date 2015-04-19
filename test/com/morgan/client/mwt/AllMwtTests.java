package com.morgan.client.mwt;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.morgan.client.mwt.tabbed.AllTabbedTests;

@RunWith(Suite.class)
@SuiteClasses({
  AllTabbedTests.class,
})
public class AllMwtTests {

}
