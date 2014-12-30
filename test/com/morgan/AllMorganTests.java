package com.morgan;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.morgan.shared.AllSharedTests;

@RunWith(Suite.class)
@SuiteClasses({
  AllSharedTests.class
})
public class AllMorganTests {

}
