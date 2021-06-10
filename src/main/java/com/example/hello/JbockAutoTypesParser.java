package com.example.hello;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.annotation.processing.Generated;
import net.jbock.contrib.StandardErrorHandler;
import net.jbock.either.Either;
import net.jbock.model.CommandModel;
import net.jbock.model.Multiplicity;
import net.jbock.model.Option;
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
final class JbockAutoTypesParser {
  Either<NotSuccess, JbockAutoTypes> parse(String... args) {
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

  JbockAutoTypes parseOrExit(String[] args) {
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
          .addDescriptionLine("<p>This class contains all \"auto types\"")
          .addDescriptionLine("that can be used without a custom converter in jbock 5.0:</p>")
          .addDescriptionLine("<ul>")
          .addDescriptionLine("<li>java.io.File</li>")
          .addDescriptionLine("<li>java.lang.String</li>")
          .addDescriptionLine("<li>java.math.BigDecimal</li>")
          .addDescriptionLine("<li>java.math.BigInteger</li>")
          .addDescriptionLine("<li>java.net.URI</li>")
          .addDescriptionLine("<li>java.nio.file.Path</li>")
          .addDescriptionLine("<li>java.time.LocalDate</li>")
          .addDescriptionLine("<li>java.util.regex.Pattern</li>")
          .addDescriptionLine("</ul>")
          .addDescriptionLine("<p>Primitives and boxed primitives are also auto types, except the booleans.")
          .addDescriptionLine("All enums are auto types. They are converted via their static {@code valueOf} method.")
          .addDescriptionLine("Special rules apply for boolean, java.util.List and java.util.Optional.</p>")
          .withProgramName("jbock-auto-types")
          .withAnsi(true)
          .withHelpEnabled(true)
          .withSuperCommand(false)
          .withAtFileExpansion(true)
          .addOption(Option.builder()
            .withParamLabel("FILE")
            .withDescriptionKey("")
            .withNames(List.of("--file"))
            .withMultiplicity(Multiplicity.REQUIRED)
            .addDescriptionLine("converter: <pre>{@code s -> {")
            .addDescriptionLine("java.io.File f = new java.io.File(s);")
            .addDescriptionLine("if (!f.exists()) {")
            .addDescriptionLine("throw new java.lang.IllegalStateException(\"File does not exist: \" + s);")
            .addDescriptionLine("}")
            .addDescriptionLine("if (!f.isFile()) {")
            .addDescriptionLine("throw new java.lang.IllegalStateException(\"Not a file: \" + s);")
            .addDescriptionLine("}")
            .addDescriptionLine("return f;")
            .addDescriptionLine("}}</pre>")
            .build())
          .addOption(Option.builder()
            .withParamLabel("STRING")
            .withDescriptionKey("")
            .withNames(List.of("--string"))
            .withMultiplicity(Multiplicity.REQUIRED)
            .addDescriptionLine("converter: java.util.function.Function.identity()")
            .build())
          .addOption(Option.builder()
            .withParamLabel("BIGDECIMAL")
            .withDescriptionKey("")
            .withNames(List.of("--bigdecimal"))
            .withMultiplicity(Multiplicity.REQUIRED)
            .addDescriptionLine("converter: java.math.BigDecimal::new")
            .build())
          .addOption(Option.builder()
            .withParamLabel("BIGINTEGER")
            .withDescriptionKey("")
            .withNames(List.of("--biginteger"))
            .withMultiplicity(Multiplicity.REQUIRED)
            .addDescriptionLine("converter: java.math.BigInteger::new")
            .build())
          .addOption(Option.builder()
            .withParamLabel("URI")
            .withDescriptionKey("")
            .withNames(List.of("--uri"))
            .withMultiplicity(Multiplicity.REQUIRED)
            .addDescriptionLine("converter: java.net.URI::create")
            .build())
          .addOption(Option.builder()
            .withParamLabel("PATH")
            .withDescriptionKey("")
            .withNames(List.of("--path"))
            .withMultiplicity(Multiplicity.REQUIRED)
            .addDescriptionLine("converter: java.nio.file.Paths::get")
            .build())
          .addOption(Option.builder()
            .withParamLabel("LOCALDATE")
            .withDescriptionKey("")
            .withNames(List.of("--localdate"))
            .withMultiplicity(Multiplicity.REQUIRED)
            .addDescriptionLine("converter: java.time.LocalDate::parse")
            .build())
          .addOption(Option.builder()
            .withParamLabel("PATTERN")
            .withDescriptionKey("")
            .withNames(List.of("--pattern"))
            .withMultiplicity(Multiplicity.REQUIRED)
            .addDescriptionLine("converter: java.util.regex.Pattern::compile")
            .build())
          .build();
  }

  private static class StatefulParser {
    Pattern suspicious = Pattern.compile("-[a-zA-Z0-9]+|--[a-zA-Z0-9-]+");

    Map<String, Opt> optionNames = new HashMap<>(8);

    Map<Opt, OptionParser> optionParsers = new EnumMap<>(Opt.class);

    StatefulParser() {
      optionNames.put("--file", Opt.FILE);
      optionParsers.put(Opt.FILE, new RegularOptionParser());
      optionNames.put("--string", Opt.STRING);
      optionParsers.put(Opt.STRING, new RegularOptionParser());
      optionNames.put("--bigdecimal", Opt.BIG_DECIMAL);
      optionParsers.put(Opt.BIG_DECIMAL, new RegularOptionParser());
      optionNames.put("--biginteger", Opt.BIG_INTEGER);
      optionParsers.put(Opt.BIG_INTEGER, new RegularOptionParser());
      optionNames.put("--uri", Opt.U_RI);
      optionParsers.put(Opt.U_RI, new RegularOptionParser());
      optionNames.put("--path", Opt.PATH);
      optionParsers.put(Opt.PATH, new RegularOptionParser());
      optionNames.put("--localdate", Opt.LOCAL_DATE);
      optionParsers.put(Opt.LOCAL_DATE, new RegularOptionParser());
      optionNames.put("--pattern", Opt.PATTERN);
      optionParsers.put(Opt.PATTERN, new RegularOptionParser());
    }

    StatefulParser parse(Iterator<String> it) throws ExToken {
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
        throw new ExToken(ErrTokenType.EXCESS_PARAM, token);
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

    JbockAutoTypes build() throws ExMissingItem, ExConvert {
      File _file = optionParsers.get(Opt.FILE).stream()
            .map(StringConverter.create(s -> {
              File f = new File(s);
              if (!f.exists()) {
                throw new IllegalStateException("File does not exist: " + s);
              }
              if (!f.isFile()) {
                throw new IllegalStateException("Not a file: " + s);
              }
              return f;
            }))
            .findAny()
            .orElseThrow(() -> new ExMissingItem(ItemType.OPTION, 0))
            .orElseThrow(left -> new ExConvert(left, ItemType.OPTION, 0));
      String _string = optionParsers.get(Opt.STRING).stream()
            .map(StringConverter.create(Function.identity()))
            .findAny()
            .orElseThrow(() -> new ExMissingItem(ItemType.OPTION, 1))
            .orElseThrow(left -> new ExConvert(left, ItemType.OPTION, 1));
      BigDecimal _big_decimal = optionParsers.get(Opt.BIG_DECIMAL).stream()
            .map(StringConverter.create(BigDecimal::new))
            .findAny()
            .orElseThrow(() -> new ExMissingItem(ItemType.OPTION, 2))
            .orElseThrow(left -> new ExConvert(left, ItemType.OPTION, 2));
      BigInteger _big_integer = optionParsers.get(Opt.BIG_INTEGER).stream()
            .map(StringConverter.create(BigInteger::new))
            .findAny()
            .orElseThrow(() -> new ExMissingItem(ItemType.OPTION, 3))
            .orElseThrow(left -> new ExConvert(left, ItemType.OPTION, 3));
      URI _u_ri = optionParsers.get(Opt.U_RI).stream()
            .map(StringConverter.create(URI::create))
            .findAny()
            .orElseThrow(() -> new ExMissingItem(ItemType.OPTION, 4))
            .orElseThrow(left -> new ExConvert(left, ItemType.OPTION, 4));
      Path _path = optionParsers.get(Opt.PATH).stream()
            .map(StringConverter.create(Paths::get))
            .findAny()
            .orElseThrow(() -> new ExMissingItem(ItemType.OPTION, 5))
            .orElseThrow(left -> new ExConvert(left, ItemType.OPTION, 5));
      LocalDate _local_date = optionParsers.get(Opt.LOCAL_DATE).stream()
            .map(StringConverter.create(LocalDate::parse))
            .findAny()
            .orElseThrow(() -> new ExMissingItem(ItemType.OPTION, 6))
            .orElseThrow(left -> new ExConvert(left, ItemType.OPTION, 6));
      Pattern _pattern = optionParsers.get(Opt.PATTERN).stream()
            .map(StringConverter.create(Pattern::compile))
            .findAny()
            .orElseThrow(() -> new ExMissingItem(ItemType.OPTION, 7))
            .orElseThrow(left -> new ExConvert(left, ItemType.OPTION, 7));
      return new JbockAutoTypesImpl(_file, _string, _big_decimal, _big_integer, _u_ri, _path,
          _local_date, _pattern);
    }
  }

  private enum Opt {
    FILE,

    STRING,

    BIG_DECIMAL,

    BIG_INTEGER,

    U_RI,

    PATH,

    LOCAL_DATE,

    PATTERN
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

  private static class JbockAutoTypesImpl extends JbockAutoTypes {
    File _file;

    String _string;

    BigDecimal _big_decimal;

    BigInteger _big_integer;

    URI _u_ri;

    Path _path;

    LocalDate _local_date;

    Pattern _pattern;

    JbockAutoTypesImpl(File _file, String _string, BigDecimal _big_decimal, BigInteger _big_integer,
        URI _u_ri, Path _path, LocalDate _local_date, Pattern _pattern) {
      this._file = _file;
      this._string = _string;
      this._big_decimal = _big_decimal;
      this._big_integer = _big_integer;
      this._u_ri = _u_ri;
      this._path = _path;
      this._local_date = _local_date;
      this._pattern = _pattern;
    }

    File file() {
      return _file;
    }

    String string() {
      return _string;
    }

    BigDecimal bigDecimal() {
      return _big_decimal;
    }

    BigInteger bigInteger() {
      return _big_integer;
    }

    URI uRI() {
      return _u_ri;
    }

    Path path() {
      return _path;
    }

    LocalDate localDate() {
      return _local_date;
    }

    Pattern pattern() {
      return _pattern;
    }
  }
}
