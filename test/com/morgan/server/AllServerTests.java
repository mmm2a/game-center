package com.morgan.server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.morgan.server.auth.AllAuthTests;
import com.morgan.server.backend.AllBackendTests;
import com.morgan.server.email.AllEmailTests;
import com.morgan.server.feature.AllFeatureTests;
import com.morgan.server.game.AllGameTests;
import com.morgan.server.util.AllUtilTests;

@RunWith(Suite.class)
@SuiteClasses({
  AllAuthTests.class,
  AllBackendTests.class,
  AllEmailTests.class,
  AllGameTests.class,
  AllUtilTests.class,
  AllFeatureTests.class
  })
public class AllServerTests {

}
