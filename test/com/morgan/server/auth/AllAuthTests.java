package com.morgan.server.auth;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AuthHostPageServletTest.class, DefaultAuthenticationServiceTest.class })
public class AllAuthTests {

}
