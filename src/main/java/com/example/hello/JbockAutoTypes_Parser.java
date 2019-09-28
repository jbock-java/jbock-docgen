package com.example.hello;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.AssertionError;
import java.lang.Byte;
import java.lang.Character;
import java.lang.Double;
import java.lang.Float;
import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.Short;
import java.lang.String;
import java.lang.System;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Generated by
 * <a href="https://github.com/h908714124/jbock">jbock 2.8.2</a>
 */
final class JbockAutoTypes_Parser {
  private PrintStream out = System.out;

  private PrintStream err = System.err;

  private int indent = 7;

  private int errorExitCode = 1;

  private Map<String, String> messages;

  private JbockAutoTypes_Parser() {
  }

  static JbockAutoTypes_Parser create() {
    return new JbockAutoTypes_Parser();
  }

  ParseResult parse(String[] args) {
    IndentPrinter outStream = new IndentPrinter(out, indent);
    IndentPrinter errStream = new IndentPrinter(err, indent);
    Messages msg = new Messages(messages == null ? Collections.emptyMap() : messages);
    Tokenizer tokenizer = new Tokenizer(outStream, errStream, msg);
    return tokenizer.parse(args);
  }

  JbockAutoTypes parseOrExit(String[] args) {
    ParseResult result = parse(args);
    if (result instanceof ParsingSuccess) {
      return ((ParsingSuccess) result).result();
    }
    if (result instanceof HelpPrinted) {
      System.exit(0);
    }
    if (result instanceof ParsingFailed) {
      System.exit(errorExitCode);
    }
    // all cases handled
    throw new AssertionError("never thrown");
  }

  JbockAutoTypes_Parser withOutputStream(PrintStream out) {
    this.out = Objects.requireNonNull(out);
    return this;
  }

  JbockAutoTypes_Parser withErrorStream(PrintStream err) {
    this.err = Objects.requireNonNull(err);
    return this;
  }

  JbockAutoTypes_Parser withIndent(int indent) {
    this.indent = indent;
    return this;
  }

  JbockAutoTypes_Parser withErrorExitCode(int errorExitCode) {
    this.errorExitCode = errorExitCode;
    return this;
  }

  JbockAutoTypes_Parser withMessages(Map<String, String> map) {
    if (messages != null) {
      throw new IllegalStateException("setting messages twice");
    }
    this.messages = Objects.requireNonNull(map);
    return this;
  }

  JbockAutoTypes_Parser withResourceBundle(ResourceBundle bundle) {
    Map<String, String> map = new HashMap<>();
    for (String name :Collections.list(bundle.getKeys())) {
      map.put(name, bundle.getString(name));
    }
    return withMessages(map);
  }

  JbockAutoTypes_Parser withMessages(InputStream stream) {
    if (stream == null) {
      return withMessages(Collections.emptyMap());
    }
    try {
      Properties properties = new Properties();
      properties.load(stream);
      Map<String, String> map = new HashMap<>();
      for (String name : properties.stringPropertyNames()) {
        map.put(name, properties.getProperty(name));
      }
      return withMessages(map);
    }
    catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  private static String readArgument(String token, Iterator<String> it) {
    boolean isLong = token.charAt(1) == '-';
    int index = token.indexOf('=');
    if (isLong && index >= 0) {
      return token.substring(index + 1);
    }
    if (!isLong && token.length() >= 3) {
      return token.substring(2);
    }
    return readNext(token, it);
  }

  private static String readNext(String token, Iterator<String> it) {
    if (!it.hasNext()) {
      throw new IllegalArgumentException("Missing value after token: " + token);
    }
    return it.next();
  }

  private static class Tokenizer {
    final IndentPrinter err;

    final IndentPrinter out;

    final Messages messages;

    Tokenizer(IndentPrinter out, IndentPrinter err, Messages messages) {
      this.err = err;
      this.out = out;
      this.messages = messages;
    }

    ParseResult parse(String[] args) {
      try {
        Optional<? extends JbockAutoTypes> result = parse(Arrays.asList(args).iterator());
        if (result.isPresent()) {
          return new ParsingSuccess(result.get());
        }
        printUsage();
        return new HelpPrinted();
      }
      catch (RuntimeException e) {
        e.printStackTrace(err.out);
        err.println("Usage:");
        err.incrementIndent();
        err.println(synopsis());
        err.decrementIndent();
        err.println();
        err.println("Error:");
        err.incrementIndent();
        err.println(e.getMessage());
        err.decrementIndent();
        err.println();
        err.println(String.format("Try '%s --help' for more information.", "JbockAutoTypes"));
        err.println();
        return new ParsingFailed(e.getMessage());
      }
      finally {
        err.flush();
        out.flush();
      }
    }

    Optional<? extends JbockAutoTypes> parse(Iterator<String> tokens) {
      boolean first = true;
      Helper helper = new Helper();
      while (tokens.hasNext()) {
        String token = tokens.next();
        if (first && "--help".equals(token)) {
          return Optional.empty();
        }
        first = false;
        if (token.isEmpty()) {
          throw new IllegalArgumentException("Invalid option: " + token);
        }
        Option option = helper.readRegularOption(token);
        if (option != null) {
          helper.read(option, token, tokens);
          continue;
        }
        throw new IllegalArgumentException("Invalid option: " + token);
      }
      return helper.build();
    }

    void printUsage() {
      out.println("NAME");
      out.incrementIndent();
      String missionStatement = messages.getMessage("jbock.mission", "");
      if (missionStatement.isEmpty()) {
        out.println("JbockAutoTypes");
      }
      else {
        out.println(String.format("%s - %s", "JbockAutoTypes", missionStatement));
      }
      out.println();
      out.decrementIndent();
      out.println("SYNOPSIS");
      out.incrementIndent();
      out.println(synopsis());
      out.println();
      out.decrementIndent();
      out.println("DESCRIPTION");
      out.incrementIndent();
      List<String> descriptionFromJavadoc = new ArrayList<>();
      descriptionFromJavadoc.add("This class contains all the basic parameter types");
      descriptionFromJavadoc.add("that can be used without a custom mapper in jbock 2.8.2.");
      descriptionFromJavadoc.add("Optional and List thereof can also be used.");
      descriptionFromJavadoc.add("All non-private enums can also be used directly.");
      descriptionFromJavadoc.add("The default mapper will use their {@code static valueOf(String)} method.");
      for (String line : messages.getMessage("jbock.description", descriptionFromJavadoc)) {
        out.println(line);
      }
      out.decrementIndent();
      out.println();
      out.println("OPTIONS");
      out.incrementIndent();
      for (Option option: Option.values()) {
        if (!option.positional()) {
          printDescription(option);
          out.println();
        }
      }
      out.decrementIndent();
      List<String> defaultHelp = new ArrayList<>();
      defaultHelp.add("Print this help page.");
      defaultHelp.add("The help flag may only be passed as the first argument.");
      defaultHelp.add("Any further arguments will be ignored.");
      out.incrementIndent();
      out.println("--help");
      out.incrementIndent();
      for (String line : messages.getMessage("jbock.help", defaultHelp)) {
        out.println(line);
      }
      out.println();
      out.decrementIndent();
      out.decrementIndent();
    }

    static String synopsis() {
      StringJoiner joiner = new StringJoiner(" ");
      joiner.add("JbockAutoTypes");
      joiner.add(Option.BOXED_BYTE.example());
      joiner.add(Option.BOXED_CHARACTER.example());
      joiner.add(Option.BOXED_DOUBLE.example());
      joiner.add(Option.BOXED_FLOAT.example());
      joiner.add(Option.BOXED_INTEGER.example());
      joiner.add(Option.BOXED_LONG.example());
      joiner.add(Option.BOXED_SHORT.example());
      joiner.add(Option.BIG_DECIMAL.example());
      joiner.add(Option.BIG_INTEGER.example());
      joiner.add(Option.CHARSET.example());
      joiner.add(Option.FILE.example());
      joiner.add(Option.INSTANT.example());
      joiner.add(Option.LOCAL_DATE.example());
      joiner.add(Option.LOCAL_DATE_TIME.example());
      joiner.add(Option.OFFSET_DATE_TIME.example());
      joiner.add(Option.PATH.example());
      joiner.add(Option.PATTERN.example());
      joiner.add(Option.STRING.example());
      joiner.add(Option.URI.example());
      joiner.add(Option.ZONED_DATE_TIME.example());
      return joiner.toString();
    }

    void printDescription(Option option) {
      if (option.positional()) {
        out.println(option.describe().toUpperCase());
      }
      else {
        out.println(option.describe());
      }
      out.incrementIndent();
      for (String line : messages.getMessage(option.bundleKey.toLowerCase(), option.description)) {
        out.println(line);
      }
      out.decrementIndent();
    }
  }

  private static class JbockAutoTypesImpl extends JbockAutoTypes {
    final Byte boxedByte;

    final Character boxedCharacter;

    final Double boxedDouble;

    final Float boxedFloat;

    final Integer boxedInteger;

    final Long boxedLong;

    final Short boxedShort;

    final BigDecimal bigDecimal;

    final BigInteger bigInteger;

    final Charset charset;

    final File file;

    final Instant instant;

    final LocalDate localDate;

    final LocalDateTime localDateTime;

    final OffsetDateTime offsetDateTime;

    final Path path;

    final Pattern pattern;

    final String string;

    final URI uri;

    final ZonedDateTime zonedDateTime;

    JbockAutoTypesImpl(Byte boxedByte, Character boxedCharacter, Double boxedDouble,
        Float boxedFloat, Integer boxedInteger, Long boxedLong, Short boxedShort,
        BigDecimal bigDecimal, BigInteger bigInteger, Charset charset, File file, Instant instant,
        LocalDate localDate, LocalDateTime localDateTime, OffsetDateTime offsetDateTime, Path path,
        Pattern pattern, String string, URI uri, ZonedDateTime zonedDateTime) {
      this.boxedByte = boxedByte;
      this.boxedCharacter = boxedCharacter;
      this.boxedDouble = boxedDouble;
      this.boxedFloat = boxedFloat;
      this.boxedInteger = boxedInteger;
      this.boxedLong = boxedLong;
      this.boxedShort = boxedShort;
      this.bigDecimal = bigDecimal;
      this.bigInteger = bigInteger;
      this.charset = charset;
      this.file = file;
      this.instant = instant;
      this.localDate = localDate;
      this.localDateTime = localDateTime;
      this.offsetDateTime = offsetDateTime;
      this.path = path;
      this.pattern = pattern;
      this.string = string;
      this.uri = uri;
      this.zonedDateTime = zonedDateTime;
    }

    @Override
    Byte boxedByte() {
      return boxedByte;
    }

    @Override
    Character boxedCharacter() {
      return boxedCharacter;
    }

    @Override
    Double boxedDouble() {
      return boxedDouble;
    }

    @Override
    Float boxedFloat() {
      return boxedFloat;
    }

    @Override
    Integer boxedInteger() {
      return boxedInteger;
    }

    @Override
    Long boxedLong() {
      return boxedLong;
    }

    @Override
    Short boxedShort() {
      return boxedShort;
    }

    @Override
    BigDecimal BigDecimal() {
      return bigDecimal;
    }

    @Override
    BigInteger BigInteger() {
      return bigInteger;
    }

    @Override
    Charset Charset() {
      return charset;
    }

    @Override
    File File() {
      return file;
    }

    @Override
    Instant Instant() {
      return instant;
    }

    @Override
    LocalDate LocalDate() {
      return localDate;
    }

    @Override
    LocalDateTime LocalDateTime() {
      return localDateTime;
    }

    @Override
    OffsetDateTime OffsetDateTime() {
      return offsetDateTime;
    }

    @Override
    Path Path() {
      return path;
    }

    @Override
    Pattern Pattern() {
      return pattern;
    }

    @Override
    String String() {
      return string;
    }

    @Override
    URI URI() {
      return uri;
    }

    @Override
    ZonedDateTime ZonedDateTime() {
      return zonedDateTime;
    }
  }

  private enum Option {
    BOXED_BYTE("java.lang.Byte", null, "null", OptionalInt.empty(), "BOXED_BYTE", Collections.emptyList()),

    BOXED_CHARACTER("java.lang.Character", null, "null", OptionalInt.empty(), "BOXED_CHARACTER", Collections.emptyList()),

    BOXED_DOUBLE("java.lang.Double", null, "null", OptionalInt.empty(), "BOXED_DOUBLE", Collections.emptyList()),

    BOXED_FLOAT("java.lang.Float", null, "null", OptionalInt.empty(), "BOXED_FLOAT", Collections.emptyList()),

    BOXED_INTEGER("java.lang.Integer", null, "null", OptionalInt.empty(), "BOXED_INTEGER", Collections.emptyList()),

    BOXED_LONG("java.lang.Long", null, "null", OptionalInt.empty(), "BOXED_LONG", Collections.emptyList()),

    BOXED_SHORT("java.lang.Short", null, "null", OptionalInt.empty(), "BOXED_SHORT", Collections.emptyList()),

    BIG_DECIMAL("java.math.BigDecimal", null, "null", OptionalInt.empty(), "BIG_DECIMAL", Collections.emptyList()),

    BIG_INTEGER("java.math.BigInteger", null, "null", OptionalInt.empty(), "BIG_INTEGER", Collections.emptyList()),

    CHARSET("java.nio.charset.Charset", null, "null", OptionalInt.empty(), "CHARSET", Collections.emptyList()),

    FILE("java.io.File", null, "null", OptionalInt.empty(), "FILE", Collections.emptyList()),

    INSTANT("java.time.Instant", null, "null", OptionalInt.empty(), "INSTANT", Collections.emptyList()),

    LOCAL_DATE("java.time.LocalDate", null, "null", OptionalInt.empty(), "LOCAL_DATE", Collections.emptyList()),

    LOCAL_DATE_TIME("java.time.LocalDateTime", null, "null", OptionalInt.empty(), "LOCAL_DATE_TIME", Collections.emptyList()),

    OFFSET_DATE_TIME("java.time.OffsetDateTime", null, "null", OptionalInt.empty(), "OFFSET_DATE_TIME", Collections.emptyList()),

    PATH("java.nio.file.Path", null, "null", OptionalInt.empty(), "PATH", Collections.emptyList()),

    PATTERN("java.util.regex.Pattern", null, "null", OptionalInt.empty(), "PATTERN", Collections.emptyList()),

    STRING("java.lang.String", null, "null", OptionalInt.empty(), "STRING", Collections.emptyList()),

    URI("java.net.URI", null, "null", OptionalInt.empty(), "URI", Collections.emptyList()),

    ZONED_DATE_TIME("java.time.ZonedDateTime", null, "null", OptionalInt.empty(), "ZONED_DATE_TIME", Collections.emptyList());

    final String longName;

    final Character shortName;

    final String bundleKey;

    final OptionalInt positionalIndex;

    final String descriptionArgumentName;

    final List<String> description;

    Option(String longName, Character shortName, String bundleKey, OptionalInt positionalIndex,
        String descriptionArgumentName, List<String> description) {
      this.longName = longName;
      this.shortName = shortName;
      this.bundleKey = bundleKey;
      this.positionalIndex = positionalIndex;
      this.description = description;
      this.descriptionArgumentName = descriptionArgumentName;
    }

    boolean positional() {
      return positionalIndex.isPresent();
    }

    String describeParam(String argname) {
      if (shortName == null) {
        return "--" + longName + argname;
      }
      if (longName == null) {
        return "-" + shortName + argname;
      }
      return "-" + shortName + argname + ", --" + longName + argname;
    }

    String example() {
      if (shortName == null) {
        return String.format("--%s=<%s>", longName, descriptionArgumentName);
      }
      return String.format("-%s <%s>", shortName, descriptionArgumentName);
    }

    Supplier<IllegalArgumentException> missingRequired() {
      return () -> positionalIndex.isPresent() ? new IllegalArgumentException(String.format("Missing parameter: <%s>", this)) : new IllegalArgumentException(String.format("Missing required option: %s (%s)", this, describeParam(""))); }

    static Map<Character, Option> shortNameMap() {
      Map<Character, Option> shortNames = new HashMap<>(Option.values().length);
      for (Option option : Option.values()) {
        if (option.shortName != null) {
          shortNames.put(option.shortName, option);
        }
      }
      return shortNames;
    }

    static Map<String, Option> longNameMap() {
      Map<String, Option> longNames = new HashMap<>(Option.values().length);
      for (Option option : Option.values()) {
        if (option.longName != null) {
          longNames.put(option.longName, option);
        }
      }
      return longNames;
    }

    static Map<Option, OptionParser> parsers() {
      Map<Option, OptionParser> parsers = new EnumMap<>(Option.class);
      for (Option option : Option.values()) {
        if (!option.positional()) {
          parsers.put(option, option.parser());
        }
      }
      return parsers;
    }

    boolean validShortToken(String token) {
      return token.length() >= 2 && token.charAt(0) == '-';
    }

    String describe() {
      return describeParam(String.format(" <%s>", descriptionArgumentName));
    }

    OptionParser parser() {
      return new RegularOptionParser(this);
    }
  }

  private static class Helper {
    final Map<String, Option> longNames = Collections.unmodifiableMap(Option.longNameMap());

    final Map<Character, Option> shortNames = Collections.unmodifiableMap(Option.shortNameMap());

    final Map<Option, OptionParser> parsers = Collections.unmodifiableMap(Option.parsers());

    void read(Option option, String token, Iterator<String> it) {
      parsers.get(option).read(token, it);
    }

    Option readRegularOption(String token) {
      if (token.length() <= 1 || token.charAt(0) != '-') {
        return null;
      }
      if (token.charAt(1) == '-') {
        return readLong(token);
      }
      return shortNames.get(token.charAt(1));
    }

    Optional<? extends JbockAutoTypesImpl> build() {
      return Optional.of(new JbockAutoTypesImpl(
          parsers.get(Option.BOXED_BYTE).value().map(Byte::valueOf).orElseThrow(Option.BOXED_BYTE.missingRequired()),
          parsers.get(Option.BOXED_CHARACTER).value().map(s -> { if (s.length() != 1) {
            throw new IllegalArgumentException("Not a single character: <" + s + ">");}
          return s.charAt(0); }).orElseThrow(Option.BOXED_CHARACTER.missingRequired()),
          parsers.get(Option.BOXED_DOUBLE).value().map(Double::valueOf).orElseThrow(Option.BOXED_DOUBLE.missingRequired()),
          parsers.get(Option.BOXED_FLOAT).value().map(Float::valueOf).orElseThrow(Option.BOXED_FLOAT.missingRequired()),
          parsers.get(Option.BOXED_INTEGER).value().map(Integer::valueOf).orElseThrow(Option.BOXED_INTEGER.missingRequired()),
          parsers.get(Option.BOXED_LONG).value().map(Long::valueOf).orElseThrow(Option.BOXED_LONG.missingRequired()),
          parsers.get(Option.BOXED_SHORT).value().map(Short::valueOf).orElseThrow(Option.BOXED_SHORT.missingRequired()),
          parsers.get(Option.BIG_DECIMAL).value().map(BigDecimal::new).orElseThrow(Option.BIG_DECIMAL.missingRequired()),
          parsers.get(Option.BIG_INTEGER).value().map(BigInteger::new).orElseThrow(Option.BIG_INTEGER.missingRequired()),
          parsers.get(Option.CHARSET).value().map(Charset::forName).orElseThrow(Option.CHARSET.missingRequired()),
          parsers.get(Option.FILE).value().map(File::new).orElseThrow(Option.FILE.missingRequired()),
          parsers.get(Option.INSTANT).value().map(Instant::parse).orElseThrow(Option.INSTANT.missingRequired()),
          parsers.get(Option.LOCAL_DATE).value().map(LocalDate::parse).orElseThrow(Option.LOCAL_DATE.missingRequired()),
          parsers.get(Option.LOCAL_DATE_TIME).value().map(LocalDateTime::parse).orElseThrow(Option.LOCAL_DATE_TIME.missingRequired()),
          parsers.get(Option.OFFSET_DATE_TIME).value().map(OffsetDateTime::parse).orElseThrow(Option.OFFSET_DATE_TIME.missingRequired()),
          parsers.get(Option.PATH).value().map(Paths::get).orElseThrow(Option.PATH.missingRequired()),
          parsers.get(Option.PATTERN).value().map(Pattern::compile).orElseThrow(Option.PATTERN.missingRequired()),
          parsers.get(Option.STRING).value().map(Function.identity()).orElseThrow(Option.STRING.missingRequired()),
          parsers.get(Option.URI).value().map(URI::create).orElseThrow(Option.URI.missingRequired()),
          parsers.get(Option.ZONED_DATE_TIME).value().map(ZonedDateTime::parse).orElseThrow(Option.ZONED_DATE_TIME.missingRequired())));
    }

    Option readLong(String token) {
      int index = token.indexOf('=');
      if (index < 0) {
        return longNames.get(token.substring(2));
      }
      else {
        return longNames.get(token.substring(2, index));
      }
    }
  }

  private abstract static class OptionParser {
    final Option option;

    OptionParser(Option option) {
      this.option = option;
    }

    abstract void read(String token, Iterator<String> it);

    Optional<String> value() {
      throw new AssertionError();
    }

    Stream<String> values() {
      throw new AssertionError();
    }

    boolean flag() {
      throw new AssertionError();
    }
  }

  private static class FlagOptionParser extends OptionParser {
    boolean flag;

    FlagOptionParser(Option option) {
      super(option);
    }

    @Override
    void read(String token, Iterator<String> it) {
      if (flag) {
        throw new IllegalArgumentException(String.format("Option %s (%s) is not repeatable", option, option.describeParam("")));
      }
      flag = true;
    }

    @Override
    boolean flag() {
      return flag;
    }
  }

  private static class RegularOptionParser extends OptionParser {
    String value;

    RegularOptionParser(Option option) {
      super(option);
    }

    @Override
    void read(String token, Iterator<String> it) {
      if (value != null) {
        throw new IllegalArgumentException(String.format("Option %s (%s) is not repeatable", option, option.describeParam("")));
      }
      value = readArgument(token, it);
    }

    @Override
    Optional<String> value() {
      return Optional.ofNullable(value);
    }
  }

  private static class RepeatableOptionParser extends OptionParser {
    List<String> values;

    RepeatableOptionParser(Option option) {
      super(option);
    }

    @Override
    void read(String token, Iterator<String> it) {
      if (values == null) {
        values = new ArrayList<>();
      }
      values.add(readArgument(token, it));
    }

    @Override
    Stream<String> values() {
      if (values == null) {
        return Stream.empty();
      }
      return values.stream();
    }
  }

  private static class IndentPrinter {
    final int baseIndent;

    final PrintWriter out;

    int indentLevel;

    IndentPrinter(PrintStream out, int baseIndent) {
      this.out = new PrintWriter(out);
      this.baseIndent = baseIndent;
    }

    void println() {
      out.println();
    }

    void println(String text) {
      if (Objects.toString(text, "").isEmpty()) {
        out.println();
        return;
      }
      for (int i = 0; i < indentLevel; i++) {
        out.print(' ');
      }
      out.println(text);
    }

    void incrementIndent() {
      indentLevel += baseIndent;
    }

    void decrementIndent() {
      indentLevel -= baseIndent;
    }

    void flush() {
      out.flush();
    }
  }

  private static class Messages {
    final Pattern br = Pattern.compile("\\r?\\n");

    final Map<String, String> messages;

    Messages(Map<String, String> messages) {
      this.messages = messages;
    }

    String getMessage(String key, String defaultValue) {
      return messages.getOrDefault(key, defaultValue);
    }

    List<String> getMessage(String key, List<String> defaultValue) {
      if (!messages.containsKey(key)) {
        return defaultValue;
      }
      return Arrays.asList(br.split(messages.get(key), -1));
    }
  }

  /**
   * This will be a sealed type in the future.
   */
  abstract static class ParseResult {
    private ParseResult() {
    }
  }

  static final class HelpPrinted extends ParseResult {
  }

  static final class ParsingFailed extends ParseResult {
    private final String message;

    private ParsingFailed(String message) {
      this.message = Objects.requireNonNull(message);
    }

    String message() {
      return message;
    }
  }

  static final class ParsingSuccess extends ParseResult {
    private final JbockAutoTypes result;

    private ParsingSuccess(JbockAutoTypes result) {
      this.result = Objects.requireNonNull(result);
    }

    JbockAutoTypes result() {
      return result;
    }
  }
}
