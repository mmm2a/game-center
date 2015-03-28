package com.morgan.server.game.modules;

import static com.google.common.truth.Truth.assertThat;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.morgan.shared.game.modules.GameIdentifier;

/**
 * Tests for the {@link ModulesLoader} class.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class ModulesLoaderTest {

  private static final GameIdentifier GAME_ID_1 = new GameIdentifier("game-1");
  private static final GameIdentifier GAME_ID_2 = new GameIdentifier("game-2");
  private static final GameIdentifier GAME_ID_3 = new GameIdentifier("game-3");

  private TestableModulesLoader loader;

  @Before public void createTestInstances() {
    loader = new TestableModulesLoader();
  }

  @Test public void loadModules() throws IOException {
    ImmutableMap<GameIdentifier, Module> result = loader.loadModules();
    assertThat(result).hasSize(3);
    assertThat(result).containsKey(GAME_ID_1);
    assertThat(result).containsKey(GAME_ID_2);
    assertThat(result).containsKey(GAME_ID_3);
    assertThat(result.get(GAME_ID_1)).isInstanceOf(TestModule1.class);
    assertThat(result.get(GAME_ID_2)).isInstanceOf(TestModule2.class);
    assertThat(result.get(GAME_ID_3)).isInstanceOf(TestModule3.class);
  }

  private static class TestableModulesLoader extends ModulesLoader {
    @Override CharSource getConfigurationSource() {
      return Resources.asCharSource(
          Resources.getResource(ModulesLoaderTest.class, "resources/test-game-modules.conf"),
          Charset.defaultCharset());
    }
  }

  static class TestModule1 extends AbstractModule {

    public TestModule1(GameIdentifier gameIdentifier) {
      assertThat(gameIdentifier).isEqualTo(GAME_ID_1);
    }

    @Override protected void configure() {
    }
  }

  static class TestModule2 extends AbstractModule {

    public TestModule2(GameIdentifier gameIdentifier) {
      assertThat(gameIdentifier).isEqualTo(GAME_ID_2);
    }

    @Override protected void configure() {
    }
  }

  static class TestModule3 extends AbstractModule {

    public TestModule3(GameIdentifier gameIdentifier) {
      assertThat(gameIdentifier).isEqualTo(GAME_ID_3);
    }

    @Override protected void configure() {
    }
  }
}
