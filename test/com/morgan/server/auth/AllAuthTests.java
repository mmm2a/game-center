package com.morgan.server.auth;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AuthenticationPageConstantsSourceTest.class, AuthHostPageServletTest.class,
    AuthModuleTest.class, AuthorizationEnforcerTest.class, DefaultAuthenticationServiceTest.class,
    LogInFilterTest.class })
public class AllAuthTests {

}
