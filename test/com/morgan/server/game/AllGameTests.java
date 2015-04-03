package com.morgan.server.game;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.morgan.server.game.modules.AllModulesTests;

@RunWith(Suite.class)
@SuiteClasses({
  AllModulesTests.class,
  GameServerTest.class,
  PortalToDescriptorFunctionTest.class,
  })
public class AllGameTests {

}
