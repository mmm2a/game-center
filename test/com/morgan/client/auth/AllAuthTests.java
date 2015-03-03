package com.morgan.client.auth;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AuthPagePresenterTest.class, LoginPagePresenterTest.class,
    LogoutPagePresenterTest.class })
public class AllAuthTests {

}
