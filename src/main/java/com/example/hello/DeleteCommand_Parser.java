package com.example.hello;

import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * <h3>Generated by <a href="https://github.com/h908714124/jbock">jbock 4.4.000</a></h3>
 */
final class DeleteCommand_Parser {
  private PrintStream err = System.err;

  private int terminalWidth = 80;

  private Consumer<ParseResult> exitHook = result ->
    System.exit(result instanceof HelpRequested ? 0 : 1);

  ParseResult parse(String[] args) {
    if (args.length == 0)
      return new HelpRequested();
    if (args.length == 1 && "--help".equals(args[0]))
      return new HelpRequested();
    StatefulParser statefulParser = new StatefulParser();
    Iterator<String> it = Arrays.asList(args).iterator();
    try {
      DeleteCommand result = statefulParser.parse(it).build();
      return new ParsingSuccess(result);
    }
    catch (RuntimeException e) {
      return new ParsingFailed(e);
    }
  }

  DeleteCommand parseOrExit(String[] args) {
    ParseResult result = parse(args);
    if (result instanceof ParsingSuccess)
      return ((ParsingSuccess) result).getResult();
    if (result instanceof HelpRequested) {
      printUsageDocumentation();
      err.flush();
      exitHook.accept(result);
      throw new RuntimeException("help requested");
    }
    err.println("\u001b[31;1mERROR\u001b[m " + ((ParsingFailed) result).getError().getMessage());
    err.println(String.join(" ", usage("Usage:")));
    err.println("Type \u001b[1mrm --help\u001b[m for more information.");
    err.flush();
    exitHook.accept(result);
    throw new RuntimeException("parsing error");
  }

  DeleteCommand_Parser withTerminalWidth(int width) {
    this.terminalWidth = width == 0 ? this.terminalWidth : width;
    return this;
  }

  DeleteCommand_Parser withMessages(Map<String, String> map) {
    return this;
  }

  DeleteCommand_Parser withExitHook(Consumer<ParseResult> exitHook) {
    this.exitHook = exitHook;
    return this;
  }

  DeleteCommand_Parser withErrorStream(PrintStream err) {
    this.err = err;
    return this;
  }

  void printUsageDocumentation() {
    err.println("\u001b[1mUSAGE\u001b[m");
    makeLines("        ", usage(" ")).forEach(err::println);
    err.println();
    err.println("\u001b[1mPARAMETERS\u001b[m");
    String indent_p = "        ";
    printItemDocumentation(Item.PATH, "  PATH ", indent_p);
    err.println();
    err.println("\u001b[1mOPTIONS\u001b[m");
    String indent_o = "                             ";
    printItemDocumentation(Item.VERBOSITY, "  -v, --verbosity VERBOSITY ", indent_o);
  }

  private void printItemDocumentation(Item item, String names, String indent) {
    List<String> tokens = new ArrayList<>();
    tokens.add(names);
    Arrays.stream(item.description)
          .map(s -> s.split("\\s+", -1))
          .flatMap(Arrays::stream)
          .forEach(tokens::add);
    makeLines(indent, tokens).forEach(err::println);
  }

  private List<String> makeLines(String indent, List<String> tokens) {
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
        line.append(fresh ? indent : " ");
      }
      line.append(token);
      i++;
    }
    if (line.length() > 0) {
      result.add(line.toString());
    }
    return result;
  }

  private List<String> usage(String prefix) {
    List<String> result = new ArrayList<>();
    result.add(prefix);
    result.add("rm");
    result.add("[OPTION]...");
    result.add("PATH");
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

  private static class StatefulParser {
    Pattern suspicious = Pattern.compile("-[a-zA-Z0-9]+|--[a-zA-Z0-9-]+");

    Map<String, Item> optionNames = new HashMap<>(2);

    Map<Item, OptionParser> optionParsers = new EnumMap<>(Item.class);

    String[] params = new String[1];

    StatefulParser() {
      optionNames.put("-v", Item.VERBOSITY);
      optionNames.put("--verbosity", Item.VERBOSITY);
      optionParsers.put(Item.VERBOSITY, new RegularOptionParser(Item.VERBOSITY));
    }

    StatefulParser parse(Iterator<String> it) {
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
            throw new RuntimeException("Invalid option: " + token);
        }
        if (position == 1)
          throw new RuntimeException("Excess param: " + token);
        params[position++] = token;
      }
      return this;
    }

    boolean tryParseOption(String token, Iterator<String> it) {
      Item option = optionNames.get(readOptionName(token));
      if (option == null)
        return false;
      optionParsers.get(option).read(token, it);
      return true;
    }

    DeleteCommand build() {
      Optional<Integer> _verbosity = this.optionParsers.get(Item.VERBOSITY).stream()
            .map(Integer::valueOf)
            .findAny();
      Path _path = Optional.ofNullable(this.params[0])
            .map(Paths::get)
            .orElseThrow(() -> new RuntimeException("Missing required parameter: \u001b[1mPATH\u001b[m"));
      return new DeleteCommandImpl(_verbosity, _path);
    }
  }

  private enum Item {
    VERBOSITY("A named option."),

    PATH("A positional parameter.");

    String[] description;

    Item(String... description) {
      this.description = description;
    }
  }

  private static class DeleteCommandImpl extends DeleteCommand {
    OptionalInt _verbosity;

    Path _path;

    DeleteCommandImpl(Optional<Integer> _verbosity, Path _path) {
      this._verbosity = _verbosity.map(OptionalInt::of).orElse(OptionalInt.empty());
      this._path = _path;
    }

    OptionalInt verbosity() {
      return _verbosity;
    }

    Path path() {
      return _path;
    }
  }

  private abstract static class OptionParser {
    final Item option;

    OptionParser(Item option) {
      this.option = option;
    }

    abstract void read(String token, Iterator<String> it);

    abstract Stream<String> stream();
  }

  private static class RegularOptionParser extends OptionParser {
    String value;

    RegularOptionParser(Item option) {
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
    private final DeleteCommand result;

    private ParsingSuccess(DeleteCommand result) {
      this.result = result;
    }

    DeleteCommand getResult() {
      return result;
    }
  }

  static final class HelpRequested extends ParseResult {
  }
}
