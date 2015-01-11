package com.morgan.server.util.cmdline;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Properties;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.morgan.server.util.macro.Macros;


/**
 * A parser and implementation of {@link CommandLineSupplier} that reads arguments from a
 * configuration file.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
class ArgumentConfigurationParser implements CommandLineSupplier {

  private final Properties properties;

  ArgumentConfigurationParser(String filename) {
    properties = new Properties();

    try (Reader reader = openFile(filename)) {
      properties.load(reader);
    } catch (IOException e) {
      throw new RuntimeException(
          String.format("Unable read and parse configuration file %s", filename), e);
    }
  }

  @VisibleForTesting Reader openFile(String filename) throws IOException {
    return new FileReader(filename);
  }

  @VisibleForTesting Map<String, String> getEnvironment() {
    return System.getenv();
  }

  @Override public CommandLine.Builder supplyCommandLineContents(CommandLine.Builder builder) {
    Function<String, String> macroReplacer = Macros.macroReplacementFunction(getEnvironment());
    for (Object keyObject : properties.keySet()) {
      String key = keyObject.toString();
      builder.addFlag(key, macroReplacer.apply(properties.getProperty(key)));
    }
    return builder;
  }
}
