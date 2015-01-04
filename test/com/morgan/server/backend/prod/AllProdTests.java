package com.morgan.server.backend.prod;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.morgan.server.backend.prod.authdb.AllAuthDbTests;

@RunWith(Suite.class)
@SuiteClasses({
  AllAuthDbTests.class,
  ProdUserBackendTest.class
  })
public class AllProdTests {

}
