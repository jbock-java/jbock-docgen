package com.example.hello;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.regex.Pattern;
import net.jbock.Command;
import net.jbock.Option;

/**
 * This class contains all "auto types"
 * that can be used without a custom converter in jbock 4.2.000.
 * Primitives and boxed primitives are also auto types, except the booleans.
 * All enums are auto types. They are converted via their static {@code valueOf} method.
 * Special rules apply for boolean, java.util.List and java.util.Optional.
 */
@Command
abstract class JbockAutoTypes {
  /**
   * Converted by: java.math.BigDecimal::new
   */
  @Option(
      names = "--bigdecimal"
  )
  abstract BigDecimal bigDecimal();

  /**
   * Converted by: java.math.BigInteger::new
   */
  @Option(
      names = "--biginteger"
  )
  abstract BigInteger bigInteger();

  /**
   * Converted by: <pre>{@code s -> {
   *   java.io.File f = new java.io.File(s);
   *   if (!f.exists()) {
   *     throw new java.lang.IllegalStateException("File does not exist: " + s);
   *   }
   *   if (!f.isFile()) {
   *     throw new java.lang.IllegalStateException("Not a file: " + s);
   *   }
   *   return f;
   * }}</pre>
   */
  @Option(
      names = "--file"
  )
  abstract File file();

  /**
   * Converted by: java.time.LocalDate::parse
   */
  @Option(
      names = "--localdate"
  )
  abstract LocalDate localDate();

  /**
   * Converted by: java.nio.file.Paths::get
   */
  @Option(
      names = "--path"
  )
  abstract Path path();

  /**
   * Converted by: java.util.regex.Pattern::compile
   */
  @Option(
      names = "--pattern"
  )
  abstract Pattern pattern();

  /**
   * Converted by: java.net.URI::create
   */
  @Option(
      names = "--uri"
  )
  abstract URI uRI();
}
