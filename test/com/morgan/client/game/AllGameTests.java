package com.morgan.client.game;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.morgan.client.game.home.AllHomeTests;

@RunWith(Suite.class)
@SuiteClasses({
  AllHomeTests.class,
})
public class AllGameTests {

}
