package com.morgan.client;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.morgan.client.nav.AllNavTests;
import com.morgan.client.page.AllPageTests;

@RunWith(Suite.class)
@SuiteClasses({
  AllNavTests.class,
  AllPageTests.class,
})
public class AllClientTests {

}
