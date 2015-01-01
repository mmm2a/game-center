package com.morgan.server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.morgan.server.feature.AllFeatureTests;
import com.morgan.server.util.AllUtilTests;

@RunWith(Suite.class)
@SuiteClasses({
  AllUtilTests.class,
  AllFeatureTests.class
  })
public class AllServerTests {

}
