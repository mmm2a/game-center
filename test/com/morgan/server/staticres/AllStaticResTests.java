package com.morgan.server.staticres;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ StaticResourcesHttpServletTest.class, StaticResourcesManagerTest.class })
public class AllStaticResTests {

}
