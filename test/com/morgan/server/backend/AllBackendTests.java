package com.morgan.server.backend;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.morgan.server.backend.prod.AllProdTests;

@RunWith(Suite.class)
@SuiteClasses({
  AllProdTests.class,
  BackendTypeTest.class
  })
public class AllBackendTests {

}
