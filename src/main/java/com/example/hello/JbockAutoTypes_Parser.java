package com.example.hello;

import java.io.File;
import java.io.PrintStream;
import java.lang.IllegalStateException;
import java.lang.Integer;
import java.lang.RuntimeException;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.System;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <h3>Generated by <a href="https://github.com/h908714124/jbock">jbock 3.6.001</a></h3>
 * <p>Use the default constructor to obtain an instance of this parser.</p>
 */
final class JbockAutoTypes_Parser {
  private static final Map<String, Option> OPTIONS_BY_NAME = optionsByName();

  private PrintStream out = System.out;

  private PrintStream err = System.err;

  private int maxLineWidth = 80;

  private BiConsumer<ParseResult, Integer> exitHook = (r, code) -> System.exit(code);

  private Map<String, String> messages = Collections.emptyMap();

  /**
   * This parse method has no side effects.
   * Consider {@link #parseOrExit()} instead which does standard error-handling
   * like printing error messages, and potentially shutting down the JVM.
   */
  ParseResult parse(String[] args) {
    if (args.length >= 1 && "--help".equals(args[0]))
      return new HelpRequested();
    StatefulParser state = new StatefulParser();
    Iterator<String> it = Arrays.asList(args).iterator();
    try {
      JbockAutoTypes result = state.parse(it);
      return new ParsingSuccess(result);
    }
    catch (RuntimeException e) {
      return new ParsingFailed(e);
    }
  }

  JbockAutoTypes parseOrExit(String[] args) {
    ParseResult result = parse(args);
    if (result instanceof ParsingSuccess)
      return ((ParsingSuccess) result).getResult();
    if (result instanceof HelpRequested) {
      printOnlineHelp(out);
      out.flush();
      exitHook.accept(result, 0);
      throw new RuntimeException("help requested");
    }
    ((ParsingFailed) result).getError().printStackTrace(err);
    printTokens(err, 8, synopsis());
    err.println("Error: " + ((ParsingFailed) result).getError().getMessage());
    err.println("Try '--help' for more information.");
    err.flush();
    exitHook.accept(result, 1);
    throw new RuntimeException("parsing error");
  }

  JbockAutoTypes_Parser withMaxLineWidth(int chars) {
    this.maxLineWidth = chars;
    return this;
  }

  JbockAutoTypes_Parser withMessages(Map<String, String> map) {
    this.messages = map;
    return this;
  }

  JbockAutoTypes_Parser withResourceBundle(ResourceBundle bundle) {
    return withMessages(Collections.list(bundle.getKeys()).stream()
      .collect(Collectors.toMap(Function.identity(), bundle::getString)));
  }

  JbockAutoTypes_Parser withExitHook(BiConsumer<ParseResult, Integer> exitHook) {
    this.exitHook = exitHook;
    return this;
  }

  JbockAutoTypes_Parser withErrorStream(PrintStream err) {
    this.err = err;
    return this;
  }

  JbockAutoTypes_Parser withHelpStream(PrintStream out) {
    this.out = out;
    return this;
  }

  void printOnlineHelp(PrintStream printStream) {
    printTokens(printStream, 8, synopsis());
    for (Option option : Option.values()) {
      String shape_padded_27_characters = String.format("  %1$-25s", option.shape);
      String message = messages.get(option.bundleKey);
      List<String> tokens = new ArrayList<>();
      tokens.add(shape_padded_27_characters);
      tokens.addAll(Optional.ofNullable(message)
            .map(String::trim)
            .map(s -> s.split("\\s+", -1))
            .map(Arrays::asList)
            .orElseGet(() -> option.description.stream()
              .map(s -> s.split("\\s+", -1))
              .flatMap(Arrays::stream)
              .collect(Collectors.toList())));
      printTokens(printStream, 28, tokens);
    }
  }

  private void printTokens(PrintStream printStream, int continuationIndent, List<String> tokens) {
    List<String> lines = makeLines(continuationIndent, tokens);
    for (String line : lines)
      printStream.println(line);
  }

  private List<String> makeLines(int continuationIndent, List<String> tokens) {
    List<String> lines = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < tokens.size(); i++) {
      String token = tokens.get(i);
      if (token.length() + sb.length() + 1 > maxLineWidth) {
        lines.add(sb.toString());
        sb.setLength(0);
        sb.append(String.join("", Collections.nCopies(continuationIndent, " ")));
        sb.append(token);
        continue;
      }
      if (i > 0)
        sb.append(' ');
      sb.append(token);
    }
    if (sb.length() > 0)
      lines.add(sb.toString());
    return lines;
  }

  private List<String> synopsis() {
    List<String> result = new ArrayList<>();
    result.add("Usage:");
    result.add("jbock-auto-types");
    result.add(String.format("%s <%s>", Option.BIG_DECIMAL.names.get(0), Option.BIG_DECIMAL.name().toLowerCase(Locale.US)));
    result.add(String.format("%s <%s>", Option.BIG_INTEGER.names.get(0), Option.BIG_INTEGER.name().toLowerCase(Locale.US)));
    result.add(String.format("%s <%s>", Option.FILE.names.get(0), Option.FILE.name().toLowerCase(Locale.US)));
    result.add(String.format("%s <%s>", Option.LOCAL_DATE.names.get(0), Option.LOCAL_DATE.name().toLowerCase(Locale.US)));
    result.add(String.format("%s <%s>", Option.PATH.names.get(0), Option.PATH.name().toLowerCase(Locale.US)));
    result.add(String.format("%s <%s>", Option.PATTERN.names.get(0), Option.PATTERN.name().toLowerCase(Locale.US)));
    result.add(String.format("%s <%s>", Option.STRING.names.get(0), Option.STRING.name().toLowerCase(Locale.US)));
    result.add(String.format("%s <%s>", Option.U_RI.names.get(0), Option.U_RI.name().toLowerCase(Locale.US)));
    return result;
  }

  private static String readOptionArgument(String token, Iterator<String> it) {
    if (token.charAt(1) == '-' && token.indexOf('=') >= 0)
      return token.substring(token.indexOf('=') + 1);
    if (token.charAt(1) != '-' && token.length() >= 3)
      return token.substring(2);
    if (!it.hasNext())
      throw new RuntimeException("Missing value after token: " + token);
    return it.next();
  }

  private static Map<String, Option> optionsByName() {
    Map<String, Option> result = new HashMap<>(Option.values().length);
    for (Option option : Option.values())
      option.names.forEach(name -> result.put(name, option));
    return result;
  }

  private static Map<Option, OptionParser> optionParsers() {
    Map<Option, OptionParser> parsers = new EnumMap<>(Option.class);
    parsers.put(Option.BIG_DECIMAL, new RegularOptionParser(Option.BIG_DECIMAL));
    parsers.put(Option.BIG_INTEGER, new RegularOptionParser(Option.BIG_INTEGER));
    parsers.put(Option.FILE, new RegularOptionParser(Option.FILE));
    parsers.put(Option.LOCAL_DATE, new RegularOptionParser(Option.LOCAL_DATE));
    parsers.put(Option.PATH, new RegularOptionParser(Option.PATH));
    parsers.put(Option.PATTERN, new RegularOptionParser(Option.PATTERN));
    parsers.put(Option.STRING, new RegularOptionParser(Option.STRING));
    parsers.put(Option.U_RI, new RegularOptionParser(Option.U_RI));
    return parsers;
  }

  private static class StatefulParser {
    boolean endOfOptionParsing;

    Map<Option, OptionParser> optionParsers = optionParsers();

    JbockAutoTypes build() {
      return new JbockAutoTypesImpl(
          optionParsers.get(Option.BIG_DECIMAL).stream().map(BigDecimal::new).findAny().orElseThrow(Option.BIG_DECIMAL::missingRequired),
          optionParsers.get(Option.BIG_INTEGER).stream().map(BigInteger::new).findAny().orElseThrow(Option.BIG_INTEGER::missingRequired),
          optionParsers.get(Option.FILE).stream().map(s -> {
            File f = new File(s);
            if (!f.exists()) {
              throw new IllegalStateException("File does not exist: " + s);
            }
            if (!f.isFile()) {
              throw new IllegalStateException("Not a file: " + s);
            }
            return f;
          }).findAny().orElseThrow(Option.FILE::missingRequired),
          optionParsers.get(Option.LOCAL_DATE).stream().map(LocalDate::parse).findAny().orElseThrow(Option.LOCAL_DATE::missingRequired),
          optionParsers.get(Option.PATH).stream().map(Paths::get).findAny().orElseThrow(Option.PATH::missingRequired),
          optionParsers.get(Option.PATTERN).stream().map(Pattern::compile).findAny().orElseThrow(Option.PATTERN::missingRequired),
          optionParsers.get(Option.STRING).stream().map(Function.identity()).findAny().orElseThrow(Option.STRING::missingRequired),
          optionParsers.get(Option.U_RI).stream().map(URI::create).findAny().orElseThrow(Option.U_RI::missingRequired));
    }

    JbockAutoTypes parse(Iterator<String> it) {
      while (it.hasNext()) {
        String token = it.next();
        if (!endOfOptionParsing && "--".equals(token)) {
          endOfOptionParsing = true;
          continue;
        }
        if (tryParseOption(token, it))
          continue;
        if (!endOfOptionParsing && token.startsWith("-"))
          throw new RuntimeException("Invalid option: " + token);
        throw new RuntimeException("Excess param: " + token);
      }
      return build();
    }

    boolean tryParseOption(String token, Iterator<String> it) {
      if (endOfOptionParsing)
        return false;
      Option option = tryReadOption(token);
      if (option == null)
        return false;
      optionParsers.get(option).read(token, it);
      return true;
    }

    Option tryReadOption(String token) {
      if (token.length() <= 1 || token.charAt(0) != '-')
        return null;
      if (token.charAt(1) != '-')
        return OPTIONS_BY_NAME.get(token.substring(0, 2));
      int index = token.indexOf('=');
      return OPTIONS_BY_NAME.get(token.substring(0, index < 0 ? token.length() : index));
    }
  }

  private static class JbockAutoTypesImpl extends JbockAutoTypes {
    BigDecimal bigDecimal;

    BigInteger bigInteger;

    File file;

    LocalDate localDate;

    Path path;

    Pattern pattern;

    String string;

    URI uRi;

    JbockAutoTypesImpl(BigDecimal bigDecimal, BigInteger bigInteger, File file, LocalDate localDate,
        Path path, Pattern pattern, String string, URI uRi) {
      this.bigDecimal = bigDecimal;
      this.bigInteger = bigInteger;
      this.file = file;
      this.localDate = localDate;
      this.path = path;
      this.pattern = pattern;
      this.string = string;
      this.uRi = uRi;
    }

    BigDecimal bigDecimal() {
      return bigDecimal;
    }

    BigInteger bigInteger() {
      return bigInteger;
    }

    File file() {
      return file;
    }

    LocalDate localDate() {
      return localDate;
    }

    Path path() {
      return path;
    }

    Pattern pattern() {
      return pattern;
    }

    String string() {
      return string;
    }

    URI uRI() {
      return uRi;
    }
  }

  private enum Option {
    BIG_DECIMAL(Collections.singletonList("--BigDecimal"), null, Collections.singletonList("Mapped by: java.math.BigDecimal::new"), "--BigDecimal BIG_DECIMAL"),

    BIG_INTEGER(Collections.singletonList("--BigInteger"), null, Collections.singletonList("Mapped by: java.math.BigInteger::new"), "--BigInteger BIG_INTEGER"),

    FILE(Collections.singletonList("--File"), null, Arrays.asList("Mapped by: <pre>{@code s -> {",
        "java.io.File f = new java.io.File(s);", "if (!f.exists()) {",
        "throw new java.lang.IllegalStateException(\"File does not exist: \" + s);", "}",
        "if (!f.isFile()) {", "throw new java.lang.IllegalStateException(\"Not a file: \" + s);",
        "}", "return f;", "}}</pre>"), "--File FILE"),

    LOCAL_DATE(Collections.singletonList("--LocalDate"), null, Collections.singletonList("Mapped by: java.time.LocalDate::parse"), "--LocalDate LOCAL_DATE"),

    PATH(Collections.singletonList("--Path"), null, Collections.singletonList("Mapped by: java.nio.file.Paths::get"), "--Path PATH"),

    PATTERN(Collections.singletonList("--Pattern"), null, Collections.singletonList("Mapped by: java.util.regex.Pattern::compile"), "--Pattern PATTERN"),

    STRING(Collections.singletonList("--String"), null, Collections.singletonList("Mapped by: java.util.function.Function.identity()"), "--String STRING"),

    U_RI(Collections.singletonList("--URI"), null, Collections.singletonList("Mapped by: java.net.URI::create"), "--URI U_RI");

    List<String> names;

    String bundleKey;

    List<String> description;

    String shape;

    Option(List<String> names, String bundleKey, List<String> description, String shape) {
      this.names = names;
      this.bundleKey = bundleKey;
      this.description = description;
      this.shape = shape;
    }

    RuntimeException missingRequired() {
      return new RuntimeException("Missing required: " + name() +
        (names.isEmpty() ? "" : " (" + String.join(", ", names) + ")"));
    }
  }

  private abstract static class OptionParser {
    final Option option;

    OptionParser(Option option) {
      this.option = option;
    }

    abstract void read(String token, Iterator<String> it);

    abstract Stream<String> stream();
  }

  private static class RegularOptionParser extends OptionParser {
    String value;

    RegularOptionParser(Option option) {
      super(option);
    }

    void read(String token, Iterator<String> it) {
      if (value != null)
        throw new RuntimeException(String.format("Option %s (%s) is not repeatable", option,
            String.join(", ", option.names)));
      value = readOptionArgument(token, it);
    }

    Stream<String> stream() {
      return value == null ? Stream.empty() : Stream.of(value);
    }
  }

  private abstract static class ParamParser {
    abstract void read(String token);

    abstract Stream<String> stream();
  }

  abstract static class ParseResult {
    private ParseResult() {
    }
  }

  static final class ParsingFailed extends ParseResult {
    private final RuntimeException error;

    private ParsingFailed(RuntimeException error) {
      this.error = error;
    }

    RuntimeException getError() {
      return error;
    }
  }

  static final class ParsingSuccess extends ParseResult {
    private final JbockAutoTypes result;

    private ParsingSuccess(JbockAutoTypes result) {
      this.result = result;
    }

    JbockAutoTypes getResult() {
      return result;
    }
  }

  static final class HelpRequested extends ParseResult {
  }
}
