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
 * This class contains all the basic parameter types
 * that can be used without a custom mapper in jbock 4.1.000.
 * Primitives and boxed primitives are also auto types, except the booleans.
 * All enums are also auto types; they are mapped via their static {@code valueOf} method.
 * Special rules apply for java.util.List and java.util.Optional, see skew rules.
 * A custom mapper must be used for all other types.
 */
@Command
abstract class JbockAutoTypes {
  /**
   * Mapped by: java.math.BigDecimal::new
   */
  @Option(
      names = "--BigDecimal"
  )
  abstract BigDecimal bigDecimal();

  /**
   * Mapped by: java.math.BigInteger::new
   */
  @Option(
      names = "--BigInteger"
  )
  abstract BigInteger bigInteger();

  /**
   * Mapped by: <pre>{@code s -> {
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
      names = "--File"
  )
  abstract File file();

  /**
   * Mapped by: java.time.LocalDate::parse
   */
  @Option(
      names = "--LocalDate"
  )
  abstract LocalDate localDate();

  /**
   * Mapped by: java.nio.file.Paths::get
   */
  @Option(
      names = "--Path"
  )
  abstract Path path();

  /**
   * Mapped by: java.util.regex.Pattern::compile
   */
  @Option(
      names = "--Pattern"
  )
  abstract Pattern pattern();

  /**
   * Mapped by: java.util.function.Function.identity()
   */
  @Option(
      names = "--String"
  )
  abstract String string();

  /**
   * Mapped by: java.net.URI::create
   */
  @Option(
      names = "--URI"
  )
  abstract URI uRI();
}
