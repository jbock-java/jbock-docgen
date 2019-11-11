package com.example.hello;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.regex.Pattern;
import net.jbock.CommandLineArguments;
import net.jbock.Parameter;

/**
 * This class contains all the basic parameter types
 * that can be used without custom mappers or collectors in jbock 2.9.0.
 * Primitives and boxed primitives are omitted here.
 * All enums can also be used; they are mapped by their {@code valueOf} method.
 */
@CommandLineArguments
abstract class JbockAutoTypes {
  /**
   * Mapped by: java.math.BigDecimal::new
   */
  @Parameter(
      longName = "BigDecimal"
  )
  abstract BigDecimal bigDecimal();

  /**
   * Mapped by: java.math.BigInteger::new
   */
  @Parameter(
      longName = "BigInteger"
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
  @Parameter(
      longName = "File"
  )
  abstract File file();

  /**
   * Mapped by: java.time.LocalDate::parse
   */
  @Parameter(
      longName = "LocalDate"
  )
  abstract LocalDate localDate();

  /**
   * Mapped by: java.nio.file.Paths::get
   */
  @Parameter(
      longName = "Path"
  )
  abstract Path path();

  /**
   * Mapped by: java.util.regex.Pattern::compile
   */
  @Parameter(
      longName = "Pattern"
  )
  abstract Pattern pattern();

  /**
   * Mapped by: java.util.function.Function.identity()
   */
  @Parameter(
      longName = "String"
  )
  abstract String string();

  /**
   * Mapped by: java.net.URI::create
   */
  @Parameter(
      longName = "URI"
  )
  abstract URI uRI();
}
