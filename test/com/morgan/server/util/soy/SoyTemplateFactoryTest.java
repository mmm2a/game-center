package com.morgan.server.util.soy;

import org.junit.Before;
import org.junit.Test;

import com.google.common.truth.Truth;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Tests for the {@link SoyTemplateFactory} class.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class SoyTemplateFactoryTest {

  @Soy(resource = "resources/test.soy", namespace = "test.morgan.soy")
  interface TestTemplate extends SoyTemplate {
    
    String noArgs();
  }
  
  private TestTemplate proxy;
  
  @Before public void createTestInstances() {
    Injector injector = Guice.createInjector(new SoyUtilModule());
    SoyTemplateFactory factory = injector.getInstance(SoyTemplateFactory.class);
    proxy = factory.createSoyTemplate(TestTemplate.class);
  }
  
  @Test public void noArgs() {
    Truth.assertThat(proxy.noArgs()).isEqualTo("No arguments");
  }
}
