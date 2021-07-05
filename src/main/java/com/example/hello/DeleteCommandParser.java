package com.example.hello;

import io.jbock.util.Either;
import io.jbock.util.Optional;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.annotation.processing.Generated;
import net.jbock.contrib.StandardErrorHandler;
import net.jbock.model.CommandModel;
import net.jbock.model.Option;
import net.jbock.model.Parameter;
import net.jbock.util.AtFileReader;
import net.jbock.util.ErrTokenType;
import net.jbock.util.ExConvert;
import net.jbock.util.ExMissingItem;
import net.jbock.util.ExNotSuccess;
import net.jbock.util.ExToken;
import net.jbock.util.HelpRequested;
import net.jbock.util.ItemType;
import net.jbock.util.NotSuccess;
import net.jbock.util.ParseRequest;
import net.jbock.util.StringConverter;

@Generated(
    value = "net.jbock.processor.JbockProcessor",
    comments = "https://github.com/jbock-java"
)
class DeleteCommandParser {
  Either<NotSuccess, DeleteCommand> parse(ParseRequest request) {
    if (request.isHelpRequested())
      return Either.left(new HelpRequested(createModel(request)));
    return new AtFileReader().read(request)
          .mapLeft(err -> err.addModel(createModel(request)))
          .map(List::iterator)
          .flatMap(it -> {
            StatefulParser statefulParser = new StatefulParser();
            try {
              return Either.right(statefulParser.parse(it).build());
            } catch (ExNotSuccess e) {
              return Either.left(e.toError(createModel(request)));
            }
          });
  }

  DeleteCommand parseOrExit(String[] args) {
    ParseRequest request = ParseRequest.standardBuilder(args)
      .withHelpRequested(args.length == 0 || "--help".equals(args[0]))
      .build();
    return parse(request).orElseThrow(notSuccess -> {
      int code = StandardErrorHandler.builder().build().handle(notSuccess);
      System.exit(code);
      return new RuntimeException();
    });
  }

  private CommandModel createModel(ParseRequest request) {
    return CommandModel.builder(request)
          .addDescriptionLine("Coffee time!")
          .withProgramName("rm")
          .addOption(Option.builder()
            .withParamLabel("VERBOSITY")
            .withNames(List.of("-v", "--verbosity"))
            .addDescriptionLine("A named option.")
            .build())
          .addParameter(Parameter.builder()
            .withParamLabel("PATH")
            .addDescriptionLine("A positional parameter.")
            .build())
          .build();
  }

  private static class StatefulParser {
    Pattern sus = Pattern.compile("-[a-zA-Z0-9]+|--[a-zA-Z0-9-]+");

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
        if (!endOfOptionParsing && tryParseOption(token, it))
          continue;
        if (!endOfOptionParsing && sus.matcher(token).matches())
          throw new ExToken(ErrTokenType.INVALID_OPTION, token);
        if (position == 1)
          throw new ExToken(ErrTokenType.EXCESS_PARAM, token);
        params[position++] = token;
      }
      return this;
    }

    String readOptionName(String token) {
      if (token.length() < 2 || !token.startsWith("-"))
        return null;
      if (!token.startsWith("--"))
        return token.substring(0, 2);
      if (!token.contains("="))
        return token;
      return token.substring(0, token.indexOf('='));
    }

    boolean tryParseOption(String token, Iterator<String> it) throws ExToken {
      Opt option = optionNames.get(readOptionName(token));
      if (option == null)
        return false;
      optionParsers.get(option).read(token, it);
      return true;
    }

    DeleteCommand build() throws ExNotSuccess {
      OptionalInt verbosity = this.optionParsers.get(Opt.VERBOSITY).stream()
            .map(StringConverter.create(Integer::valueOf))
            .collect(Either.toValidList())
            .orElseThrow(left -> new ExConvert(left, ItemType.OPTION, 0))
            .stream().findAny()
            .map(OptionalInt::of).orElse(OptionalInt.empty());
      Path path = Optional.ofNullable(this.params[0])
            .map(StringConverter.create(Paths::get))
            .orElseThrow(() -> new ExMissingItem(ItemType.PARAMETER, 0))
            .orElseThrow(left -> new ExConvert(left, ItemType.PARAMETER, 0));
      return new DeleteCommandImpl(verbosity, path);
    }
  }

  private enum Opt {
    VERBOSITY
  }

  private abstract static class OptionParser {
    abstract void read(String token, Iterator<String> it) throws ExToken;

    abstract Stream<String> stream();

    String readOptionArgument(String token, Iterator<String> it) throws ExToken {
      boolean unix = !token.startsWith("--");
      if (unix && token.length() > 2)
        return token.substring(2);
      if (!unix && token.contains("="))
        return token.substring(token.indexOf('=') + 1);
      if (it.hasNext())
        return it.next();
      throw new ExToken(ErrTokenType.MISSING_ARGUMENT, token);
    }
  }

  private static class RegularOptionParser extends OptionParser {
    String value;

    void read(String token, Iterator<String> it) throws ExToken {
      if (value != null)
        throw new ExToken(ErrTokenType.OPTION_REPETITION, token);
      value = readOptionArgument(token, it);
    }

    Stream<String> stream() {
      return Optional.ofNullable(value).stream();
    }
  }

  private static class DeleteCommandImpl extends DeleteCommand {
    OptionalInt verbosity;

    Path path;

    DeleteCommandImpl(OptionalInt verbosity, Path path) {
      this.verbosity = verbosity;
      this.path = path;
    }

    OptionalInt verbosity() {
      return verbosity;
    }

    Path path() {
      return path;
    }
  }
}
