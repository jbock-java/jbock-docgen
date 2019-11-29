package com.example.hello;

import java.io.PrintStream;
import java.lang.AssertionError;
import java.lang.Character;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.System;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.ResourceBundle;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Generated by
 * <a href="https://github.com/h908714124/jbock">jbock 3.0.3</a>
 */
final class MyArguments_Parser {
  private PrintStream out = System.out;

  private PrintStream err = System.err;

  private int maxLineWidth = 80;

  private Consumer<ParseResult> runBeforeExit = r -> {};

  private Map<String, String> messages;

  private MyArguments_Parser() {
  }

  static MyArguments_Parser create() {
    return new MyArguments_Parser();
  }

  ParseResult parse(String[] args) {
    Messages msg = new Messages(messages == null ? Collections.emptyMap() : messages);
    return new Tokenizer(msg).parse(args);
  }

  MyArguments parseOrExit(String[] args) {
    ParseResult result = parse(args);
    if (result instanceof ParsingSuccess) {
      return ((ParsingSuccess) result).getResult();
    }
    if (result instanceof HelpRequested) {
      HelpRequested helpResult = (HelpRequested) result;
      printOnlineHelp(out, helpResult.getSynopsis(), helpResult.getRows());
      runBeforeExit.accept(result);
      System.exit(0);
    }
    if (result instanceof ParsingFailed) {
      ParsingFailed errorResult = (ParsingFailed) result;
      errorResult.getError().printStackTrace(err);
      err.println("Error: " + errorResult.getError().getMessage());
      printOnlineHelp(err, errorResult.getSynopsis(), errorResult.getRows());
      err.println("Try '--help' for more information.");
      runBeforeExit.accept(result);
      System.exit(1);
    }
    throw new AssertionError("all cases handled");
  }

  private void printOnlineHelp(PrintStream printStream, String synopsis,
      List<Map.Entry<String, String>> rows) {
    int keyWidth = rows.stream().map(Map.Entry::getKey).mapToInt(String::length).max().orElse(0) + 2;
    String keyFormat = "%1$-" + keyWidth + "s";
    printWrap(printStream, 8, "", "Usage: " + synopsis);
    for (Map.Entry<String, String> row : rows) {
      String key = String.format(keyFormat, row.getKey());
      printWrap(printStream, keyWidth, key, row.getValue());
    }
    printStream.flush();
  }

  private void printWrap(PrintStream printStream, int continuationIndent, String init,
      String input) {
    if (input.isEmpty()) {
      String trim = init.trim();
      printStream.println(init.substring(0, init.indexOf(trim)) + trim);
      return;
    }
    String[] tokens = input.split("\\s+", -1);
    StringBuilder row = new StringBuilder(init);
    for (String token : tokens) {
      if (token.length() + row.length() + 1 > maxLineWidth) {
        if (row.toString().isEmpty()) {
          printStream.println(token);
        }
        else {
          printStream.println(row);
          row.setLength(0);
          for (int i = 0; i < continuationIndent; i++) {
            row.append(' ');
          }
          row.append(token);
        }
      }
      else {
        if (row.length() > 0 && !Character.isWhitespace(row.charAt(row.length() - 1))) {
          row.append(' ');
        }
        row.append(token);
      }
    }
    if (row.length() > 0) {
      printStream.println(row);
    }
  }

  MyArguments_Parser withErrorStream(PrintStream err) {
    this.err = Objects.requireNonNull(err);
    return this;
  }

  MyArguments_Parser maxLineWidth(int chars) {
    this.maxLineWidth = chars;
    return this;
  }

  MyArguments_Parser withMessages(Map<String, String> map) {
    this.messages = Objects.requireNonNull(map);
    return this;
  }

  MyArguments_Parser withResourceBundle(ResourceBundle bundle) {
    Map<String, String> map = new HashMap<>();
    for (String name : Collections.list(bundle.getKeys())) {
      map.put(name, bundle.getString(name));
    }
    return withMessages(map);
  }

  MyArguments_Parser runBeforeExit(Consumer<ParseResult> runBeforeExit) {
    this.runBeforeExit = runBeforeExit;
    return this;
  }

  private static String readValidArgument(String token, Iterator<String> it) {
    boolean isLong = token.charAt(1) == '-';
    int index = token.indexOf('=');
    if (isLong && index >= 0) {
      return token.substring(index + 1);
    }
    if (!isLong && token.length() >= 3) {
      return token.substring(2);
    }
    if (!it.hasNext()) {
      throw new IllegalArgumentException("Missing value after token: " + token);
    }
    return it.next();
  }

  MyArguments_Parser withOutputStream(PrintStream out) {
    this.out = Objects.requireNonNull(out);
    return this;
  }

  private static class Tokenizer {
    final Messages messages;

    Tokenizer(Messages messages) {
      this.messages = messages;
    }

    ParseResult parse(String[] args) {
      if (args.length >= 1 && "--help".equals(args[0])) {
        return new HelpRequested(synopsis(), buildRows());
      }
      try {
        return new ParsingSuccess(parse(Arrays.asList(args).iterator()));
      }
      catch (RuntimeException e) {
        return new ParsingFailed(synopsis(), buildRows(), e);
      }
    }

    MyArguments parse(Iterator<String> it) {
      int position = 0;
      ParserState state = new ParserState();
      while (it.hasNext()) {
        String token = it.next();
        if ("--".equals(token)) {
          while (it.hasNext()) {
            String t = it.next();
            if (position >= state.positionalParsers.size()) {
              throw new IllegalArgumentException("Invalid option: " + t);
            }
            position += state.positionalParsers.get(position).read(t);
          }
          return state.build();
        }
        Option option = state.tryReadOption(token);
        if (option != null) {
          state.parsers.get(option).read(token, it);
          continue;
        }
        if (!token.isEmpty() && token.charAt(0) == '-') {
          throw new IllegalArgumentException("Invalid option: " + token);
        }
        if (position >= state.positionalParsers.size()) {
          throw new IllegalArgumentException("Invalid option: " + token);
        }
        position += state.positionalParsers.get(position).read(token);
      }
      return state.build();
    }

    static String synopsis() {
      StringJoiner joiner = new StringJoiner(" ");
      joiner.add("my-arguments");
      joiner.add("[options...]");
      joiner.add("<path>");
      return joiner.toString();
    }

    List<Map.Entry<String, String>> buildRows() {
      List<Map.Entry<String, String>> rows = new ArrayList<>();
      for (Option option: Option.values()) {
        if (option.positionalIndex.isPresent()) {
          rows.add(printDescription(option));
        }
      }
      for (Option option: Option.values()) {
        if (!option.positionalIndex.isPresent()) {
          rows.add(printDescription(option));
        }
      }
      return rows;
    }

    Map.Entry<String, String> printDescription(Option option) {
      String message = messages.getMessage(option.bundleKey, option.description);
      String description;
      if (option.positionalIndex.isPresent()) {
        description = option.name().toLowerCase(Locale.US);
      }
      else if (option.flag) {
        description = option.describeParam("");
      }
      else {
        description = option.describeParam(' ' + option.name());
      }
      return new AbstractMap.SimpleImmutableEntry<String, String>(description, message);
    }
  }

  private static class ParserState {
    final Map<String, Option> optionNames = Collections.unmodifiableMap(Option.optionNames());

    final Map<Option, OptionParser> parsers = Collections.unmodifiableMap(Option.parsers());

    final List<PositionalOptionParser> positionalParsers = Collections.unmodifiableList(Option.positionalParsers());

    MyArguments build() {
      return new MyArgumentsImpl(
          positionalParsers.get(0).values().map(Paths::get).findAny().orElseThrow(Option.PATH.missingRequired()),
          parsers.get(Option.VERBOSITY).values().map(Integer::valueOf).findAny());
    }

    Option tryReadOption(String token) {
      if (token.length() <= 1 || token.charAt(0) != '-') {
        return null;
      }
      if (token.charAt(1) == '-') {
        int index = token.indexOf('=');
        return optionNames.get(token.substring(0, index < 0 ? token.length() : index));
      }
      return optionNames.get(token.substring(0, 2));
    }
  }

  private static class MyArgumentsImpl extends MyArguments {
    final Path path;

    final OptionalInt verbosity;

    MyArgumentsImpl(Path path, Optional<Integer> verbosity) {
      this.path = path;
      this.verbosity = verbosity.isPresent() ? OptionalInt.of(verbosity.get()) : OptionalInt.empty();
    }

    @Override
    Path path() {
      return path;
    }

    @Override
    OptionalInt verbosity() {
      return verbosity;
    }
  }

  private enum Option {
    PATH(Collections.emptyList(), null, false, OptionalInt.of(0), Collections.emptyList()),

    VERBOSITY(Arrays.asList("-v", "--verbosity"), null, false, OptionalInt.empty(), Collections.emptyList());

    final List<String> names;

    final boolean flag;

    final String bundleKey;

    final OptionalInt positionalIndex;

    final List<String> description;

    Option(List<String> names, String bundleKey, boolean flag, OptionalInt positionalIndex,
        List<String> description) {
      this.names = names;
      this.flag = flag;
      this.bundleKey = bundleKey;
      this.positionalIndex = positionalIndex;
      this.description = description;
    }

    String describeParam(String argname) {
      if (names.size() == 1) {
        return "    " + names.get(0) + argname;
      }
      return names.get(0) + ", " + names.get(1) + argname;
    }

    Supplier<IllegalArgumentException> missingRequired() {
      return () -> positionalIndex.isPresent()
        ? new IllegalArgumentException(String.format("Missing parameter: <%s>", this))
        : new IllegalArgumentException(String.format("Missing required option: %s (%s)", this, describeParam("").trim()));
    }

    static Map<String, Option> optionNames() {
      Map<String, Option> result = new HashMap<>(Option.values().length);
      for (Option option : Option.values()) {
        for (String name : option.names) {
          result.put(name, option);
        }
      }
      return result;
    }

    static Map<Option, OptionParser> parsers() {
      Map<Option, OptionParser> parsers = new EnumMap<>(Option.class);
      parsers.put(VERBOSITY, new RegularOptionParser(VERBOSITY));
      return parsers;
    }

    static List<PositionalOptionParser> positionalParsers() {
      List<PositionalOptionParser> parsers = new ArrayList<>();
      parsers.add(new RegularPositionalOptionParser());
      return parsers;
    }
  }

  private abstract static class OptionParser {
    final Option option;

    OptionParser(Option option) {
      this.option = option;
    }

    abstract void read(String token, Iterator<String> it);

    Stream<String> values() {
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
      if (token.charAt(1) != '-' && token.length() > 2 || token.contains("=")) {
        throw new IllegalArgumentException("Invalid token: " + token);
      }
      if (flag) {
        throw new IllegalArgumentException(String.format("Option %s (%s) is not repeatable", option, option.describeParam("").trim()));
      }
      flag = true;
    }

    @Override
    Stream<String> values() {
      return flag ? Stream.of("") : Stream.empty();
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
        throw new IllegalArgumentException(String.format("Option %s (%s) is not repeatable", option, option.describeParam("").trim()));
      }
      value = readValidArgument(token, it);
    }

    @Override
    Stream<String> values() {
      return value == null ? Stream.empty() : Stream.of(value);
    }
  }

  private static class RepeatableOptionParser extends OptionParser {
    List<String> values = new ArrayList<>();

    RepeatableOptionParser(Option option) {
      super(option);
    }

    @Override
    void read(String token, Iterator<String> it) {
      values.add(readValidArgument(token, it));
    }

    @Override
    Stream<String> values() {
      return values.stream();
    }
  }

  private abstract static class PositionalOptionParser {
    abstract int read(String token);

    Stream<String> values() {
      throw new AssertionError();
    }
  }

  private static class RegularPositionalOptionParser extends PositionalOptionParser {
    String value;

    @Override
    int read(String value) {
      this.value = value;
      return 1;
    }

    @Override
    Stream<String> values() {
      return value == null ? Stream.empty() : Stream.of(value);
    }
  }

  private static class RepeatablePositionalOptionParser extends PositionalOptionParser {
    List<String> values = new ArrayList<>();

    @Override
    int read(String value) {
      values.add(value);
      return 0;
    }

    @Override
    Stream<String> values() {
      return values.stream();
    }
  }

  private static class Messages {
    final Map<String, String> messages;

    Messages(Map<String, String> messages) {
      this.messages = messages;
    }

    String getMessage(String key, List<String> defaultValue) {
      return messages.getOrDefault(key, String.join(" ", defaultValue));
    }
  }

  abstract static class ParseResult {
    private ParseResult() {
    }
  }

  static final class ParsingFailed extends ParseResult {
    private final String synopsis;

    private final List<Map.Entry<String, String>> rows;

    private final RuntimeException error;

    private ParsingFailed(String synopsis, List<Map.Entry<String, String>> rows,
        RuntimeException error) {
      this.error = error;
      this.synopsis = synopsis;
      this.rows = rows;
    }

    String getSynopsis() {
      return synopsis;
    }

    List<Map.Entry<String, String>> getRows() {
      return rows;
    }

    RuntimeException getError() {
      return error;
    }
  }

  static final class ParsingSuccess extends ParseResult {
    private final MyArguments result;

    private ParsingSuccess(MyArguments result) {
      this.result = result;
    }

    MyArguments getResult() {
      return result;
    }
  }

  static final class HelpRequested extends ParseResult {
    private final String synopsis;

    private final List<Map.Entry<String, String>> rows;

    private HelpRequested(String synopsis, List<Map.Entry<String, String>> rows) {
      this.synopsis = synopsis;
      this.rows = rows;
    }

    String getSynopsis() {
      return synopsis;
    }

    List<Map.Entry<String, String>> getRows() {
      return rows;
    }
  }
}
