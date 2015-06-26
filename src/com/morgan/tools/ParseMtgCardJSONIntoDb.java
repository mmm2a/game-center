package com.morgan.tools;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.morgan.server.mtg.CardSet;
import com.morgan.server.mtg.json.JsonModule;
import com.morgan.server.mtg.json.MtgCardJsonHelper;
import com.morgan.server.util.cmdline.CommandLine;
import com.morgan.server.util.cmdline.CommandLineParser;
import com.morgan.server.util.flag.Flag;
import com.morgan.server.util.flag.FlagAccessor;
import com.morgan.server.util.flag.FlagAccessorFactory;
import com.morgan.server.util.flag.Flags;
import com.morgan.server.util.log.AdvancedLogger;
import com.morgan.server.util.log.InjectLogger;

/**
 * Main application for filling in the magic the gathering card database from its JSON
 * representation.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
public final class ParseMtgCardJSONIntoDb {

  private interface ToolFlagAccessor extends FlagAccessor {

    @Flag(name = "json-file",
        description = "Name or path to the file that has the JSON set database",
        required = true)
    String jsonFile();

    @Flag(name = "id-file",
        description =
            "Name or path to the file to write out which contains all known multiverse ids",
        defaultValue = "",
        required = false)
    String idFile();
  }

  private static final class ParseMtgCardJSONIntoDbModule extends AbstractModule {
    @Override protected void configure() {
      install(new ToolModule());
      install(new JsonModule());
    }

    @Provides @Singleton
    protected ToolFlagAccessor provideToolFlagAccessor(FlagAccessorFactory factory) {
      return factory.getFlagAccessor(ToolFlagAccessor.class);
    }
  }

  private final ToolFlagAccessor flagAccessor;
  private final MtgCardJsonHelper mtgCardJsonHelper;

  @InjectLogger private AdvancedLogger log = AdvancedLogger.NULL;

  @Inject ParseMtgCardJSONIntoDb(
      ToolFlagAccessor flagAccessor,
      MtgCardJsonHelper mtgCardJsonHelper) {
    this.flagAccessor = flagAccessor;
    this.mtgCardJsonHelper = mtgCardJsonHelper;
  }

  void runTool() throws FileNotFoundException, IOException {
    ImmutableMap<String, CardSet> cardSetMap;

    try (FileReader reader = new FileReader(flagAccessor.jsonFile())) {
      log.info("Parsing JSON file");
      JsonElement element = new JsonParser().parse(reader);
      cardSetMap = mtgCardJsonHelper.parseIntoCardSets(element.getAsJsonObject());
    }

    if (!Strings.isNullOrEmpty(flagAccessor.idFile())) {
      log.info("Writing ids to %s", flagAccessor.idFile());
      try (PrintStream idFile = new PrintStream(flagAccessor.idFile())) {
        cardSetMap.values().stream()
            .flatMap(s -> s.getCards().stream())
            .filter(c -> c.getMultiverseId().isPresent())
            .map(c -> c.getMultiverseId().get())
            .forEach(i -> idFile.format("%s\n", i));
      }
      log.info("Done writing ids to %s", flagAccessor.idFile());
    }
  }

  public static void main(String []args) throws Exception {
    CommandLineParser parser = new CommandLineParser(args);
    Flags.initializeWith(parser.supplyCommandLineContents(CommandLine.builder()).build());

    // Then create the GUICE injector to bootstrap the server.
    Injector injector = Guice.createInjector(new ParseMtgCardJSONIntoDbModule());
    injector.getInstance(ParseMtgCardJSONIntoDb.class).runTool();
  }
}
