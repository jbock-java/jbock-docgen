package com.example.hello;

import java.io.File;
import java.io.PrintStream;
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
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * <h3>Generated by <a href="https://github.com/h908714124/jbock">jbock 4.3.002</a></h3>
 */
final class JbockAutoTypes_Parser {
  private PrintStream err = System.err;

  private int terminalWidth = 80;

  private Consumer<ParseResult> exitHook = result ->
    System.exit(result instanceof HelpRequested ? 0 : 1);

  ParseResult parse(String[] args) {
    if (args.length == 0)
      return new HelpRequested();
    if (args.length == 1 && ("--help".equals(args[0]) || "-h".equals(args[0])))
      return new HelpRequested();
    StatefulParser statefulParser = new StatefulParser();
    Iterator<String> it = Arrays.asList(args).iterator();
    try {
      JbockAutoTypes result = statefulParser.parse(it).build();
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
      printOnlineHelp();
      err.flush();
      exitHook.accept(result);
      throw new RuntimeException("help requested");
    }
    err.println("Error: " + ((ParsingFailed) result).getError().getMessage());
    printTokens("        ", usage());
    err.println("Try 'jbock-auto-types --help' for more information.");
    err.flush();
    exitHook.accept(result);
    throw new RuntimeException("parsing error");
  }

  JbockAutoTypes_Parser withTerminalWidth(int width) {
    this.terminalWidth = width == 0 ? this.terminalWidth : width;
    return this;
  }

  JbockAutoTypes_Parser withMessages(Map<String, String> map) {
    return this;
  }

  JbockAutoTypes_Parser withExitHook(Consumer<ParseResult> exitHook) {
    this.exitHook = exitHook;
    return this;
  }

  JbockAutoTypes_Parser withErrorStream(PrintStream err) {
    this.err = err;
    return this;
  }

  void printOnlineHelp() {
    List<String> description = new ArrayList<>();
    Collections.addAll(description, "<p>This class contains all \"auto types\"".split("\\s+", -1));
    Collections.addAll(description, "that can be used without a custom converter in jbock 4.3.002:</p>".split("\\s+", -1));
    Collections.addAll(description, "<ul>".split("\\s+", -1));
    Collections.addAll(description, "<li>java.io.File</li>".split("\\s+", -1));
    Collections.addAll(description, "<li>java.math.BigDecimal</li>".split("\\s+", -1));
    Collections.addAll(description, "<li>java.math.BigInteger</li>".split("\\s+", -1));
    Collections.addAll(description, "<li>java.net.URI</li>".split("\\s+", -1));
    Collections.addAll(description, "<li>java.nio.file.Path</li>".split("\\s+", -1));
    Collections.addAll(description, "<li>java.time.LocalDate</li>".split("\\s+", -1));
    Collections.addAll(description, "<li>java.util.regex.Pattern</li>".split("\\s+", -1));
    Collections.addAll(description, "</ul>".split("\\s+", -1));
    Collections.addAll(description, "<p>Primitives and boxed primitives are also auto types, except the booleans.".split("\\s+", -1));
    Collections.addAll(description, "All enums are auto types. They are converted via their static {@code valueOf} method.".split("\\s+", -1));
    Collections.addAll(description, "Special rules apply for boolean, java.util.List and java.util.Optional.</p>".split("\\s+", -1));
    printTokens("", description);
    err.println();
    err.println("USAGE");
    printTokens("        ", usage());
    err.println();
    err.println("OPTIONS");
    printOption(Option.file, "  --file FILE             ");
    printOption(Option.bigDecimal, "  --bigdecimal BIGDECIMAL ");
    printOption(Option.bigInteger, "  --biginteger BIGINTEGER ");
    printOption(Option.uRi, "  --uri URI               ");
    printOption(Option.path, "  --path PATH             ");
    printOption(Option.localDate, "  --localdate LOCALDATE   ");
    printOption(Option.pattern, "  --pattern PATTERN       ");
  }

  private void printOption(Option option, String names) {
    List<String> tokens = new ArrayList<>();
    tokens.add(names);
    Arrays.stream(option.description)
          .map(s -> s.split("\\s+", -1))
          .flatMap(Arrays::stream)
          .forEach(tokens::add);
    String continuationIndent = String.join("", Collections.nCopies(names.length() + 1, " "));
    printTokens(continuationIndent, tokens);
  }

  private void printTokens(String continuationIndent, List<String> tokens) {
    List<String> lines = makeLines(continuationIndent, tokens);
    for (String line : lines)
      err.println(line);
  }

  private List<String> makeLines(String continuationIndent, List<String> tokens) {
    List<String> result = new ArrayList<>();
    StringBuilder line = new StringBuilder();
    int i = 0;
    while (i < tokens.size()) {
      String token = tokens.get(i);
      boolean fresh = line.length() == 0;
      if (!fresh && token.length() + line.length() + 1 > terminalWidth) {
        result.add(line.toString());
        line.setLength(0);
        continue;
      }
      if (i > 0) {
        line.append(fresh ? continuationIndent : " ");
      }
      line.append(token);
      i++;
    }
    if (line.length() > 0) {
      result.add(line.toString());
    }
    return result;
  }

  private List<String> usage() {
    List<String> result = new ArrayList<>();
    result.add(" ");
    result.add("jbock-auto-types");
    result.add(String.format("%s %s", "--file", "FILE"));
    result.add(String.format("%s %s", "--bigdecimal", "BIGDECIMAL"));
    result.add(String.format("%s %s", "--biginteger", "BIGINTEGER"));
    result.add(String.format("%s %s", "--uri", "URI"));
    result.add(String.format("%s %s", "--path", "PATH"));
    result.add(String.format("%s %s", "--localdate", "LOCALDATE"));
    result.add(String.format("%s %s", "--pattern", "PATTERN"));
    return result;
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

  private static String readOptionArgument(String token, Iterator<String> it) {
    if (token.charAt(1) == '-' && token.indexOf('=') >= 0)
      return token.substring(token.indexOf('=') + 1);
    if (token.charAt(1) != '-' && token.length() >= 3)
      return token.substring(2);
    if (!it.hasNext())
      throw new RuntimeException("Missing argument after token: " + token);
    return it.next();
  }

  private static RuntimeException missingRequired(String name) {
    return new RuntimeException("Missing required: " + name);
  }

  private static class StatefulParser {
    Pattern suspicious = Pattern.compile("-[a-zA-Z0-9]+|--[a-zA-Z0-9-]+");

    Map<String, Option> optionNames = new HashMap<>(7);

    Map<Option, OptionParser> optionParsers = new EnumMap<>(Option.class);

    StatefulParser() {
      optionNames.put("--file", Option.file);
      optionParsers.put(Option.file, new RegularOptionParser(Option.file));
      optionNames.put("--bigdecimal", Option.bigDecimal);
      optionParsers.put(Option.bigDecimal, new RegularOptionParser(Option.bigDecimal));
      optionNames.put("--biginteger", Option.bigInteger);
      optionParsers.put(Option.bigInteger, new RegularOptionParser(Option.bigInteger));
      optionNames.put("--uri", Option.uRi);
      optionParsers.put(Option.uRi, new RegularOptionParser(Option.uRi));
      optionNames.put("--path", Option.path);
      optionParsers.put(Option.path, new RegularOptionParser(Option.path));
      optionNames.put("--localdate", Option.localDate);
      optionParsers.put(Option.localDate, new RegularOptionParser(Option.localDate));
      optionNames.put("--pattern", Option.pattern);
      optionParsers.put(Option.pattern, new RegularOptionParser(Option.pattern));
    }

    StatefulParser parse(Iterator<String> it) {
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
            throw new RuntimeException("Invalid option: " + token);
        }
        throw new RuntimeException("Excess param: " + token);
      }
      return this;
    }

    boolean tryParseOption(String token, Iterator<String> it) {
      Option option = optionNames.get(readOptionName(token));
      if (option == null)
        return false;
      optionParsers.get(option).read(token, it);
      return true;
    }

    JbockAutoTypes build() {
      return new JbockAutoTypesImpl(
          optionParsers.get(Option.file).stream()
            .map(s -> {
              File f = new File(s);
              if (!f.exists()) {
                throw new IllegalStateException("File does not exist: " + s);
              }
              if (!f.isFile()) {
                throw new IllegalStateException("Not a file: " + s);
              }
              return f;
            })
            .findAny()
            .orElseThrow(() -> missingRequired("FILE (--file)")),
          optionParsers.get(Option.bigDecimal).stream()
            .map(BigDecimal::new)
            .findAny()
            .orElseThrow(() -> missingRequired("BIG_DECIMAL (--bigdecimal)")),
          optionParsers.get(Option.bigInteger).stream()
            .map(BigInteger::new)
            .findAny()
            .orElseThrow(() -> missingRequired("BIG_INTEGER (--biginteger)")),
          optionParsers.get(Option.uRi).stream()
            .map(URI::create)
            .findAny()
            .orElseThrow(() -> missingRequired("U_RI (--uri)")),
          optionParsers.get(Option.path).stream()
            .map(Paths::get)
            .findAny()
            .orElseThrow(() -> missingRequired("PATH (--path)")),
          optionParsers.get(Option.localDate).stream()
            .map(LocalDate::parse)
            .findAny()
            .orElseThrow(() -> missingRequired("LOCAL_DATE (--localdate)")),
          optionParsers.get(Option.pattern).stream()
            .map(Pattern::compile)
            .findAny()
            .orElseThrow(() -> missingRequired("PATTERN (--pattern)")));
    }
  }

  private enum Option {
    file("converter: <pre>{@code s -> {",
    "java.io.File f = new java.io.File(s);",
    "if (!f.exists()) {",
    "throw new java.lang.IllegalStateException(\"File does not exist: \" + s);",
    "}",
    "if (!f.isFile()) {",
    "throw new java.lang.IllegalStateException(\"Not a file: \" + s);",
    "}",
    "return f;",
    "}}</pre>"),

    bigDecimal("converter: java.math.BigDecimal::new"),

    bigInteger("converter: java.math.BigInteger::new"),

    uRi("converter: java.net.URI::create"),

    path("converter: java.nio.file.Paths::get"),

    localDate("converter: java.time.LocalDate::parse"),

    pattern("converter: java.util.regex.Pattern::compile");

    String[] description;

    Option(String... description) {
      this.description = description;
    }
  }

  private static class JbockAutoTypesImpl extends JbockAutoTypes {
    File file;

    BigDecimal bigDecimal;

    BigInteger bigInteger;

    URI uRi;

    Path path;

    LocalDate localDate;

    Pattern pattern;

    JbockAutoTypesImpl(File file, BigDecimal bigDecimal, BigInteger bigInteger, URI uRi, Path path,
        LocalDate localDate, Pattern pattern) {
      this.file = file;
      this.bigDecimal = bigDecimal;
      this.bigInteger = bigInteger;
      this.uRi = uRi;
      this.path = path;
      this.localDate = localDate;
      this.pattern = pattern;
    }

    File file() {
      return file;
    }

    BigDecimal bigDecimal() {
      return bigDecimal;
    }

    BigInteger bigInteger() {
      return bigInteger;
    }

    URI uRI() {
      return uRi;
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
        throw new RuntimeException(String.format("Option '%s' is a repetition", token));
      value = readOptionArgument(token, it);
    }

    Stream<String> stream() {
      return value == null ? Stream.empty() : Stream.of(value);
    }
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
