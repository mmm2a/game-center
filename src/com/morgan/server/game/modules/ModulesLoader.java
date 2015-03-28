package com.morgan.server.game.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import com.google.inject.Module;
import com.morgan.shared.game.modules.GameIdentifier;

/**
 * A loader class that loads (reads the META-INF/game-modules.conf) file and returns a list of
 * GUICE modules {@link Module} that should be installed.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public class ModulesLoader {

  private static final String RESOURCE_PATH = "META-INF/game-modules.conf";

  private static final Pattern CONF_LINE =
      Pattern.compile("^([-a-z0-9]+)\\s*<=\\s*([a-zA-Z0-9.$]+)$");

  public ModulesLoader() {
  }

  @VisibleForTesting CharSource getConfigurationSource() {
    return Resources.asCharSource(Resources.getResource(RESOURCE_PATH), Charset.defaultCharset());
  }

  private String stripCommentsAndTrim(String line) {
    int index = line.indexOf('#');
    if (index >= 0) {
      line = line.substring(0, index);
    }
    return line.trim();
  }

  private Module createModuleFor(
      GameIdentifier gameIdentifier, String gameModuleClassPath, int lineNumber) throws IOException {
    try {
      Class<?> gameModuleClass = Class.forName(gameModuleClassPath);
      if (!Module.class.isAssignableFrom(gameModuleClass)) {
        throw new IOException(String.format(
            "Error parsing line %d of game configuration: %s is not a Guice module class",
            lineNumber, gameModuleClass));
      }
      Constructor<?> gameModuleConstructor = gameModuleClass.getConstructor(GameIdentifier.class);
      return (Module) gameModuleConstructor.newInstance(gameIdentifier);
    } catch (Exception e) {
      Throwables.propagateIfInstanceOf(e, IOException.class);
      throw new IOException(
          String.format("Error parsing line %d of game configuration", lineNumber),
          e);
    }
  }

  public ImmutableMap<GameIdentifier, Module> loadModules() throws IOException {
    ImmutableMap.Builder<GameIdentifier, Module> resultBuilder = ImmutableMap.builder();

    try (BufferedReader reader = getConfigurationSource().openBufferedStream()) {
      String line;
      int lineNumber = 0;
      while ((line = reader.readLine()) != null) {
        lineNumber++;
        line = stripCommentsAndTrim(line);
        if (line.isEmpty()) {
          continue;
        }

        Matcher matcher = CONF_LINE.matcher(line);
        if (!matcher.matches()) {
          throw new IOException(String.format(
              "Error trying to parse game configuration on line %d", lineNumber));
        }

        String gameIdentifierString = matcher.group(1);
        String moduleClassName = matcher.group(2);

        GameIdentifier gameIdentifier = new GameIdentifier(gameIdentifierString);
        resultBuilder.put(
            gameIdentifier, createModuleFor(gameIdentifier, moduleClassName, lineNumber));
      }
    }

    return resultBuilder.build();
  }
}
