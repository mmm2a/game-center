package com.morgan.server.account;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AccountCreationHelperTest.class, DefaultAccountLookupHelperTest.class,
    DefaultAccountServiceTest.class })
public class AllAccountTests {

}
