package com.morgan;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.morgan.client.AllClientTests;
import com.morgan.server.AllServerTests;
import com.morgan.shared.AllSharedTests;

@RunWith(Suite.class)
@SuiteClasses({
  AllClientTests.class,
  AllServerTests.class,
  AllSharedTests.class
})
public class AllMorganTests {

}
