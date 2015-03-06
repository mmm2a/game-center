package com.morgan.server.util.flag;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.truth.Truth;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.morgan.server.util.cmdline.CommandLine;

/**
 * Tests for the {@link FlagAccessorFactory} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@RunWith(MockitoJUnitRunner.class)
public class FlagAccessorFactoryTest {

  interface FancyParser extends FlagValueParser {
  }

  interface TestAccessor extends FlagAccessor {
    @Flag(name = "string-flag",
        description = "some description")
    String stringFlag();

    @Flag(name = "int-flag",
        description = "some description")
    int intFlag();

    @Flag(name = "integer-flag",
        description = "some description")
    Integer integerFlag();

    @Flag(name = "boolean-flag",
        description = "some description")
    boolean isSomething();

    @Flag(name = "optional-flag",
        description = "some description",
        defaultValue = "optional",
        required = false)
    String optionalFlag();

    @Flag(name = "fancy-flag",
        description = "some description",
        parser = FancyParser.class)
    ImmutableList<String> fancyFlag();

    @Flag(name = "collection-flag",
        description = "some description",
        required = false,
        defaultValue = "SECONDS,MINUTES, HOURS")
    ImmutableSet<TimeUnit> collectionFlag();
  }

  @Mock private FancyParser mockFancyParser;

  private FlagAccessorFactory factory;

  @Before public void createTestInstances() {
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override protected void configure() {
        bind(FancyParser.class).toInstance(mockFancyParser);
      }
    });
    factory = injector.getInstance(FlagAccessorFactory.class);
  }

  @Before public void setUpCommonMockInteractions() {
    when(mockFancyParser.parseStringRepresentation(any(Type.class), eq("fancy-value")))
        .thenReturn(ImmutableList.of("one", "two", "three"));
  }

  private TestAccessor getAccessorFor(@Nullable String optionalValue) {
    CommandLine.Builder builder = CommandLine.builder()
        .addFlag("string-flag", "string-value")
        .addFlag("int-flag", "7")
        .addFlag("integer-flag", "42")
        .addFlag("boolean-flag", "true")
        .addFlag("fancy-flag", "fancy-value")
        .addArgument("arg1")
        .addArgument("arg2")
        .addArgument("arg3");

    if (optionalValue != null) {
      builder.addFlag("optional-flag", optionalValue);
    }

    Flags.overrideWith(builder.build());

    return factory.getFlagAccessor(TestAccessor.class);
  }

  @Test public void getArguments() {
    Truth.assertThat(getAccessorFor(null).getArguments())
        .containsExactly("arg1", "arg2", "arg3")
        .inOrder();
  }

  @Test public void stringFlag() {
    Truth.assertThat(getAccessorFor(null).stringFlag()).isEqualTo("string-value");
  }

  @Test public void fancyFlag() {
    Truth.assertThat(getAccessorFor(null).fancyFlag())
        .containsExactly("one", "two", "three")
        .inOrder();
  }

  @Test public void intFlag() {
    Truth.assertThat(getAccessorFor(null).intFlag()).is(7);
  }

  @Test public void integerFlag() {
    Truth.assertThat(getAccessorFor(null).integerFlag()).isEqualTo(Integer.valueOf(42));
  }

  @Test public void isSomething() {
    Truth.assertThat(getAccessorFor(null).isSomething()).isTrue();
  }

  @Test public void optionalFlag() {
    Truth.assertThat(getAccessorFor(null).optionalFlag()).isEqualTo("optional");
    Truth.assertThat(getAccessorFor("filled in").optionalFlag()).isEqualTo("filled in");
  }

  @Test public void collectionFlag() {
    Truth.assertThat((Iterable<?>) getAccessorFor(null).collectionFlag())
        .isEqualTo(ImmutableSet.of(TimeUnit.SECONDS, TimeUnit.MINUTES, TimeUnit.HOURS));
  }
}
