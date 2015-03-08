package com.morgan.client.nav;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DefaultNavigationTest.class, PlaceRepresentationHelperTest.class,
    ClientUrlCreatorTest.class })
public class AllNavTests {

}
