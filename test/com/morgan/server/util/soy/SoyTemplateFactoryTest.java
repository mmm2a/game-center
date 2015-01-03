package com.morgan.server.util.soy;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.truth.Truth;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.template.soy.data.SanitizedContent;
import com.google.template.soy.data.UnsafeSanitizedContentOrdainer;
import com.google.template.soy.data.SanitizedContent.ContentKind;
import com.google.template.soy.tofu.SoyTofu.Renderer;

/**
 * Tests for the {@link SoyTemplateFactory} class.
 * 
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class SoyTemplateFactoryTest {

  @Soy(resource = "resources/test.soy", namespace = "test.morgan.soy")
  interface TestTemplate extends SoyTemplate {
    
    String noArgs();
    String simpleArgs(@SoyParameter(name = "one") String one, @SoyParameter(name = "two") int two);
    String optionalArg(@SoyParameter(name = "opt") Optional<String> opt);
    
    @SoyMethod(name = "noArgs")
    SanitizedContent sanitizedAndNamed();
    
    @SoyMethod(name = "noArgs")
    Renderer renderer();
    
    @SoyMethod(name = "trueHtml")
    SafeHtml trueHtml();
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
  
  @Test public void simpleArgs() {
    Truth.assertThat(proxy.simpleArgs("alpha", 7)).isEqualTo("Arg alpha and 7.");
  }
  
  @Test public void optionalArg_missing() {
    Truth.assertThat(proxy.optionalArg(Optional.<String>absent())).isEqualTo("no arg");
  }
  
  @Test public void optionalArg_present() {
    Truth.assertThat(proxy.optionalArg(Optional.of("alpha"))).isEqualTo("arg is alpha");;
  }
  
  @Test public void sanitizedAndNamed() {
    Truth.assertThat(proxy.sanitizedAndNamed())
        .isEqualTo(UnsafeSanitizedContentOrdainer.ordainAsSafe("No arguments", ContentKind.HTML));
  }
  
  @Test public void renderer() {
    Truth.assertThat(proxy.renderer().render()).isEqualTo("No arguments");
  }
  
  @Test public void safeHtml() {
    Truth.assertThat(proxy.trueHtml()).isEqualTo(SafeHtmlUtils.fromSafeConstant("<div>foo</div>"));
  }
}
