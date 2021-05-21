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
 * <p>This class contains all "auto types"
 * that can be used without a custom converter in jbock 4.3.001:</p>
 *
 * <ul>
 *   <li>java.io.File</li>
 *   <li>java.math.BigDecimal</li>
 *   <li>java.math.BigInteger</li>
 *   <li>java.net.URI</li>
 *   <li>java.nio.file.Path</li>
 *   <li>java.time.LocalDate</li>
 *   <li>java.util.regex.Pattern</li>
 * </ul>
 *
 * <p>Primitives and boxed primitives are also auto types, except the booleans.
 * All enums are auto types. They are converted via their static {@code valueOf} method.
 * Special rules apply for boolean, java.util.List and java.util.Optional.</p>
 */
@Command
abstract class JbockAutoTypes {
  /**
   * converter: <pre>{@code s -> {
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
   * converter: java.math.BigDecimal::new
   */
  @Option(
      names = "--bigdecimal"
  )
  abstract BigDecimal bigDecimal();

  /**
   * converter: java.math.BigInteger::new
   */
  @Option(
      names = "--biginteger"
  )
  abstract BigInteger bigInteger();

  /**
   * converter: java.net.URI::create
   */
  @Option(
      names = "--uri"
  )
  abstract URI uRI();

  /**
   * converter: java.nio.file.Paths::get
   */
  @Option(
      names = "--path"
  )
  abstract Path path();

  /**
   * converter: java.time.LocalDate::parse
   */
  @Option(
      names = "--localdate"
  )
  abstract LocalDate localDate();

  /**
   * converter: java.util.regex.Pattern::compile
   */
  @Option(
      names = "--pattern"
  )
  abstract Pattern pattern();
}
