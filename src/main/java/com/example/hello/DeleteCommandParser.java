package com.example.hello;

import io.jbock.util.Either;
import io.jbock.util.Eithers;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import javax.annotation.processing.Generated;
import net.jbock.contrib.StandardErrorHandler;
import net.jbock.model.CommandModel;
import net.jbock.model.ItemType;
import net.jbock.model.Multiplicity;
import net.jbock.model.Option;
import net.jbock.model.Parameter;
import net.jbock.parse.OptionState;
import net.jbock.parse.OptionStateNonRepeatable;
import net.jbock.parse.RestlessParser;
import net.jbock.util.ExConvert;
import net.jbock.util.ExFailure;
import net.jbock.util.ExMissingItem;
import net.jbock.util.ParseRequest;
import net.jbock.util.ParsingFailed;
import net.jbock.util.StringConverter;

@Generated(
    value = "net.jbock.processor.JbockProcessor",
    comments = "https://github.com/jbock-java"
)
final class DeleteCommandParser {
  private final Map<String, Opt> optionNames = optionNames();

  Either<ParsingFailed, DeleteCommand> parse(List<String> tokens) {
    RestlessParser<Opt> parser = RestlessParser.create(optionNames, optionStates(), 1);
    try {
      parser.parse(tokens);
      return Either.right(harvest(parser));
    } catch (ExFailure e) {
      return Either.left(e.toError(createModel()));
    }
  }

  DeleteCommand parseOrExit(String[] args) {
    if (args.length == 0 || "--help".equals(args[0])) {
      StandardErrorHandler.builder().build()
        .printUsageDocumentation(createModel());
      System.exit(0);
    }
    return ParseRequest.from(args).expand()
      .mapLeft(err -> err.addModel(createModel()))
      .flatMap(this::parse)
      .orElseThrow(failure -> {
        StandardErrorHandler.builder().build().printErrorMessage(failure);
        System.exit(1);
        return new RuntimeException();
      });
  }

  private DeleteCommand harvest(RestlessParser<Opt> parser) throws ExFailure {
    OptionalInt _verbosity = parser.option(Opt.VERBOSITY)
          .map(StringConverter.create(Integer::valueOf))
          .collect(Eithers.toValidList())
          .orElseThrow(left -> new ExConvert(left, ItemType.OPTION, 0))
          .stream().findAny()
          .map(OptionalInt::of).orElse(OptionalInt.empty());
    Path _path = parser.param(0)
          .map(StringConverter.create(Paths::get))
          .orElseThrow(() -> new ExMissingItem(ItemType.PARAMETER, 0))
          .orElseThrow(left -> new ExConvert(left, ItemType.PARAMETER, 0));
    return new DeleteCommand_Impl(_verbosity, _path);
  }

  private static Map<String, Opt> optionNames() {
    Map<String, Opt> result = new HashMap<>(2);
    result.put("-v", Opt.VERBOSITY);
    result.put("--verbosity", Opt.VERBOSITY);
    return result;
  }

  private Map<Opt, OptionState> optionStates() {
    Map<Opt, OptionState> result = new EnumMap<>(Opt.class);
    result.put(Opt.VERBOSITY, new OptionStateNonRepeatable());
    return result;
  }

  CommandModel createModel() {
    return CommandModel.builder()
          .withProgramName("delete-command")
          .addOption(Option.unary(Multiplicity.OPTIONAL)
            .withParamLabel("VERBOSITY")
            .withNames(List.of("-v", "--verbosity"))
            .addDescriptionLine("A named option.")
            .build())
          .addParameter(Parameter.builder(Multiplicity.REQUIRED)
            .withParamLabel("PATH")
            .addDescriptionLine("A positional parameter.")
            .build())
          .build();
  }

  private enum Opt {
    VERBOSITY
  }
}
