package com.morgan.server.util.cmdline;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ArgumentConfigurationParserTest.class, CommandLineParserTest.class,
    CommandLineTest.class })
public class AllCommandLineTests {
}
