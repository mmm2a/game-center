package com.morgan.client;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.morgan.client.nav.AllNavTests;

@RunWith(Suite.class)
@SuiteClasses({
  AllNavTests.class,
})
public class AllClientTests {

}
