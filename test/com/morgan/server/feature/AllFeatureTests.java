package com.morgan.server.feature;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ FeatureConfigurationTest.class, ServerFeatureCheckerTest.class,
    SimpleEnableDisableEvaluatorTest.class })
public class AllFeatureTests {

}
