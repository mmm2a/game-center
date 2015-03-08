package com.morgan.client;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.morgan.client.account.AllAccountTests;
import com.morgan.client.alert.AllAlertTests;
import com.morgan.client.auth.AllAuthTests;
import com.morgan.client.nav.AllNavTests;
import com.morgan.client.page.AllPageTests;

@RunWith(Suite.class)
@SuiteClasses({
  AllAccountTests.class,
  AllAlertTests.class,
  AllAuthTests.class,
  AllNavTests.class,
  AllPageTests.class,
})
public class AllClientTests {

}
