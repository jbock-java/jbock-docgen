package com.example.hello;

import io.jbock.util.Either;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.annotation.processing.Generated;
import net.jbock.contrib.StandardErrorHandler;
import net.jbock.model.CommandModel;
import net.jbock.model.ItemType;
import net.jbock.model.Multiplicity;
import net.jbock.model.Option;
import net.jbock.util.ErrTokenType;
import net.jbock.util.ExConvert;
import net.jbock.util.ExMissingItem;
import net.jbock.util.ExNotSuccess;
import net.jbock.util.ExToken;
import net.jbock.util.ParseRequest;
import net.jbock.util.ParsingFailed;
import net.jbock.util.StringConverter;

@Generated(
    value = "net.jbock.processor.JbockProcessor",
    comments = "https://github.com/jbock-java"
)
class JbockAutoTypesParser {
  Either<ParsingFailed, JbockAutoTypes> parse(List<String> tokens) {
    Iterator<String> it = tokens.iterator();
    StatefulParser statefulParser = new StatefulParser();
    try {
      return Either.right(statefulParser.parse(it).build());
    } catch (ExNotSuccess e) {
      return Either.left(e.toError(createModel()));
    }
  }

  JbockAutoTypes parseOrExit(String[] args) {
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

  CommandModel createModel() {
    return CommandModel.builder()
          .withProgramName("jbock-auto-types")
          .addOption(Option.builder()
            .withParamLabel("FILE")
            .withNames(List.of("--file"))
            .withMultiplicity(Multiplicity.REQUIRED)
            .build())
          .addOption(Option.builder()
            .withParamLabel("STRING")
            .withNames(List.of("--string"))
            .withMultiplicity(Multiplicity.REQUIRED)
            .build())
          .addOption(Option.builder()
            .withParamLabel("BIGDECIMAL")
            .withNames(List.of("--bigdecimal"))
            .withMultiplicity(Multiplicity.REQUIRED)
            .build())
          .addOption(Option.builder()
            .withParamLabel("BIGINTEGER")
            .withNames(List.of("--biginteger"))
            .withMultiplicity(Multiplicity.REQUIRED)
            .build())
          .addOption(Option.builder()
            .withParamLabel("URI")
            .withNames(List.of("--uri"))
            .withMultiplicity(Multiplicity.REQUIRED)
            .build())
          .addOption(Option.builder()
            .withParamLabel("PATH")
            .withNames(List.of("--path"))
            .withMultiplicity(Multiplicity.REQUIRED)
            .build())
          .addOption(Option.builder()
            .withParamLabel("LOCALDATE")
            .withNames(List.of("--localdate"))
            .withMultiplicity(Multiplicity.REQUIRED)
            .build())
          .addOption(Option.builder()
            .withParamLabel("PATTERN")
            .withNames(List.of("--pattern"))
            .withMultiplicity(Multiplicity.REQUIRED)
            .build())
          .build();
  }

  private static class StatefulParser {
    Pattern sus = Pattern.compile("-[a-zA-Z0-9]+|--[a-zA-Z0-9-]+");

    Map<String, Opt> optionNames = new HashMap<>(8);

    Map<Opt, OptionParser> optionParsers = new EnumMap<>(Opt.class);

    StatefulParser() {
      optionNames.put("--file", Opt.FILE);
      optionParsers.put(Opt.FILE, new RegularOptionParser());
      optionNames.put("--string", Opt.STRING);
      optionParsers.put(Opt.STRING, new RegularOptionParser());
      optionNames.put("--bigdecimal", Opt.BIGDECIMAL);
      optionParsers.put(Opt.BIGDECIMAL, new RegularOptionParser());
      optionNames.put("--biginteger", Opt.BIGINTEGER);
      optionParsers.put(Opt.BIGINTEGER, new RegularOptionParser());
      optionNames.put("--uri", Opt.URI);
      optionParsers.put(Opt.URI, new RegularOptionParser());
      optionNames.put("--path", Opt.PATH);
      optionParsers.put(Opt.PATH, new RegularOptionParser());
      optionNames.put("--localdate", Opt.LOCALDATE);
      optionParsers.put(Opt.LOCALDATE, new RegularOptionParser());
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
        if (!endOfOptionParsing && tryParseOption(token, it))
          continue;
        if (!endOfOptionParsing && sus.matcher(token).matches())
          throw new ExToken(ErrTokenType.INVALID_OPTION, token);
        throw new ExToken(ErrTokenType.EXCESS_PARAM, token);
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

    JbockAutoTypes build() throws ExNotSuccess {
      File file = this.optionParsers.get(Opt.FILE).stream()
            .map(new FileConverter())
            .findAny()
            .orElseThrow(() -> new ExMissingItem(ItemType.OPTION, 0))
            .orElseThrow(left -> new ExConvert(left, ItemType.OPTION, 0));
      String string = this.optionParsers.get(Opt.STRING).stream()
            .map(StringConverter.create(Function.identity()))
            .findAny()
            .orElseThrow(() -> new ExMissingItem(ItemType.OPTION, 1))
            .orElseThrow(left -> new ExConvert(left, ItemType.OPTION, 1));
      BigDecimal bigDecimal = this.optionParsers.get(Opt.BIGDECIMAL).stream()
            .map(StringConverter.create(BigDecimal::new))
            .findAny()
            .orElseThrow(() -> new ExMissingItem(ItemType.OPTION, 2))
            .orElseThrow(left -> new ExConvert(left, ItemType.OPTION, 2));
      BigInteger bigInteger = this.optionParsers.get(Opt.BIGINTEGER).stream()
            .map(StringConverter.create(BigInteger::new))
            .findAny()
            .orElseThrow(() -> new ExMissingItem(ItemType.OPTION, 3))
            .orElseThrow(left -> new ExConvert(left, ItemType.OPTION, 3));
      URI uRI = this.optionParsers.get(Opt.URI).stream()
            .map(StringConverter.create(URI::create))
            .findAny()
            .orElseThrow(() -> new ExMissingItem(ItemType.OPTION, 4))
            .orElseThrow(left -> new ExConvert(left, ItemType.OPTION, 4));
      Path path = this.optionParsers.get(Opt.PATH).stream()
            .map(StringConverter.create(Paths::get))
            .findAny()
            .orElseThrow(() -> new ExMissingItem(ItemType.OPTION, 5))
            .orElseThrow(left -> new ExConvert(left, ItemType.OPTION, 5));
      LocalDate localDate = this.optionParsers.get(Opt.LOCALDATE).stream()
            .map(StringConverter.create(LocalDate::parse))
            .findAny()
            .orElseThrow(() -> new ExMissingItem(ItemType.OPTION, 6))
            .orElseThrow(left -> new ExConvert(left, ItemType.OPTION, 6));
      Pattern pattern = this.optionParsers.get(Opt.PATTERN).stream()
            .map(StringConverter.create(Pattern::compile))
            .findAny()
            .orElseThrow(() -> new ExMissingItem(ItemType.OPTION, 7))
            .orElseThrow(left -> new ExConvert(left, ItemType.OPTION, 7));
      return new JbockAutoTypesImpl(file, string, bigDecimal, bigInteger, uRI, path, localDate,
          pattern);
    }
  }

  private enum Opt {
    FILE,

    STRING,

    BIGDECIMAL,

    BIGINTEGER,

    URI,

    PATH,

    LOCALDATE,

    PATTERN
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

  private static class JbockAutoTypesImpl extends JbockAutoTypes {
    File file;

    String string;

    BigDecimal bigDecimal;

    BigInteger bigInteger;

    URI uRI;

    Path path;

    LocalDate localDate;

    Pattern pattern;

    JbockAutoTypesImpl(File file, String string, BigDecimal bigDecimal, BigInteger bigInteger,
        URI uRI, Path path, LocalDate localDate, Pattern pattern) {
      this.file = file;
      this.string = string;
      this.bigDecimal = bigDecimal;
      this.bigInteger = bigInteger;
      this.uRI = uRI;
      this.path = path;
      this.localDate = localDate;
      this.pattern = pattern;
    }

    File file() {
      return file;
    }

    String string() {
      return string;
    }

    BigDecimal bigDecimal() {
      return bigDecimal;
    }

    BigInteger bigInteger() {
      return bigInteger;
    }

    URI uRI() {
      return uRI;
    }

    Path path() {
      return path;
    }

    LocalDate localDate() {
      return localDate;
    }

    Pattern pattern() {
      return pattern;
    }
  }

  private static class FileConverter extends StringConverter<File> {
    @Override
    protected File convert(String token) {
      File file = new File(token);
      if (!file.exists())
        throw new IllegalStateException("File does not exist: " + token);
      if (!file.isFile())
        throw new IllegalStateException("Not a file: " + token);
      return file;
    }
  }
}
