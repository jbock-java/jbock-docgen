package com.example.hello;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.annotation.processing.Generated;
import net.jbock.contrib.StandardErrorHandler;
import net.jbock.either.Either;
import net.jbock.model.CommandModel;
import net.jbock.model.Multiplicity;
import net.jbock.model.Option;
import net.jbock.model.Parameter;
import net.jbock.util.AtFileReader;
import net.jbock.util.ErrTokenType;
import net.jbock.util.ExConvert;
import net.jbock.util.ExMissingItem;
import net.jbock.util.ExNotSuccess;
import net.jbock.util.ExToken;
import net.jbock.util.FileReadingError;
import net.jbock.util.HelpRequested;
import net.jbock.util.ItemType;
import net.jbock.util.NotSuccess;
import net.jbock.util.StringConverter;

@Generated(
    value = "net.jbock.processor.JbockProcessor",
    comments = "https://github.com/jbock-java"
)
final class DeleteCommandParser {
  Either<NotSuccess, DeleteCommand> parse(String... args) {
    if (args.length == 0)
      return Either.left(new HelpRequested(createModel()));
    if (args.length == 1 && "--help".equals(args[0]))
      return Either.left(new HelpRequested(createModel()));
    boolean atFile = args.length == 1
                && args[0].length() >= 2
                && args[0].startsWith("@");
    Either<FileReadingError, List<String>> either = atFile ?
          new AtFileReader().readAtFile(args[0].substring(1)) :
          Either.right(Arrays.asList(args));
    return either.mapLeft(err -> err.addModel(createModel()))
          .map(List::iterator)
          .flatMap(it -> {
            StatefulParser statefulParser = new StatefulParser();
            try {
              return Either.right(statefulParser.parse(it).build());
            } catch (ExNotSuccess e) {
              return Either.left(e.toError(createModel()));
            }
          });
  }

  DeleteCommand parseOrExit(String[] args) {
    return parse(args).orElseThrow(notSuccess ->
      StandardErrorHandler.builder(notSuccess).build().handle());
  }

  private static String readOptionName(String token) {
    if (token.length() <= 1 || token.charAt(0) != '-')
      return null;
    if (token.charAt(1) != '-')
      return token.substring(0, 2);
    int index = token.indexOf('=');
    if (index < 0)
      return token;
    return token.substring(0, index);
  }

  private static String readOptionArgument(String token, Iterator<String> it) throws ExToken {
    if (token.charAt(1) == '-' && token.indexOf('=') >= 0)
      return token.substring(token.indexOf('=') + 1);
    if (token.charAt(1) != '-' && token.length() >= 3)
      return token.substring(2);
    if (!it.hasNext())
      throw new ExToken(ErrTokenType.MISSING_ARGUMENT, token);
    return it.next();
  }

  private CommandModel createModel() {
    return CommandModel.builder()
          .withDescriptionKey("")
          .addDescriptionLine("Coffee time!")
          .withProgramName("rm")
          .withAnsi(true)
          .withHelpEnabled(true)
          .withSuperCommand(false)
          .withAtFileExpansion(true)
          .addOption(Option.builder()
            .withParamLabel("VERBOSITY")
            .withDescriptionKey("")
            .withNames(List.of("-v", "--verbosity"))
            .withMultiplicity(Multiplicity.OPTIONAL)
            .addDescriptionLine("A named option.")
            .build())
          .addParameter(Parameter.builder()
            .withParamLabel("PATH")
            .withDescriptionKey("")
            .withMultiplicity(Multiplicity.REQUIRED)
            .addDescriptionLine("A positional parameter.")
            .build())
          .build();
  }

  private static class StatefulParser {
    Pattern suspicious = Pattern.compile("-[a-zA-Z0-9]+|--[a-zA-Z0-9-]+");

    Map<String, Opt> optionNames = new HashMap<>(2);

    Map<Opt, OptionParser> optionParsers = new EnumMap<>(Opt.class);

    String[] params = new String[1];

    StatefulParser() {
      optionNames.put("-v", Opt.VERBOSITY);
      optionNames.put("--verbosity", Opt.VERBOSITY);
      optionParsers.put(Opt.VERBOSITY, new RegularOptionParser());
    }

    StatefulParser parse(Iterator<String> it) throws ExToken {
      int position = 0;
      boolean endOfOptionParsing = false;
      while (it.hasNext()) {
        String token = it.next();
        if (!endOfOptionParsing && "--".equals(token)) {
          endOfOptionParsing = true;
          continue;
        }
        if (!endOfOptionParsing && tryParseOption(token, it)) {
          continue;
        }
        if (!endOfOptionParsing) {
          if (suspicious.matcher(token).matches())
            throw new ExToken(ErrTokenType.INVALID_OPTION, token);
        }
        if (position == 1)
          throw new ExToken(ErrTokenType.EXCESS_PARAM, token);
        params[position++] = token;
      }
      return this;
    }

    boolean tryParseOption(String token, Iterator<String> it) throws ExToken {
      Opt option = optionNames.get(readOptionName(token));
      if (option == null)
        return false;
      optionParsers.get(option).read(token, it);
      return true;
    }

    DeleteCommand build() throws ExMissingItem, ExConvert {
      OptionalInt _verbosity = optionParsers.get(Opt.VERBOSITY).stream()
            .map(StringConverter.create(Integer::valueOf))
            .collect(Either.toValidList())
            .orElseThrow(left -> new ExConvert(left, ItemType.OPTION, 0))
            .stream().findAny()
            .map(OptionalInt::of).orElse(OptionalInt.empty());
      Path _path = Optional.ofNullable(params[0])
            .map(StringConverter.create(Paths::get))
            .orElseThrow(() -> new ExMissingItem(ItemType.PARAMETER, 0))
            .orElseThrow(left -> new ExConvert(left, ItemType.PARAMETER, 0));
      return new DeleteCommandImpl(_verbosity, _path);
    }
  }

  private enum Opt {
    VERBOSITY
  }

  private abstract static class OptionParser {
    abstract void read(String token, Iterator<String> it) throws ExToken;

    abstract Stream<String> stream();
  }

  private static class RegularOptionParser extends OptionParser {
    String value;

    void read(String token, Iterator<String> it) throws ExToken {
      if (value != null)
        throw new ExToken(ErrTokenType.OPTION_REPETITION, token);
      value = readOptionArgument(token, it);
    }

    Stream<String> stream() {
      return value == null ? Stream.empty() : Stream.of(value);
    }
  }

  private static class DeleteCommandImpl extends DeleteCommand {
    OptionalInt _verbosity;

    Path _path;

    DeleteCommandImpl(OptionalInt _verbosity, Path _path) {
      this._verbosity = _verbosity;
      this._path = _path;
    }

    OptionalInt verbosity() {
      return _verbosity;
    }

    Path path() {
      return _path;
    }
  }
}
