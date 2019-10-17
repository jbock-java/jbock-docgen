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
 * that can be used without custom mappers or collectors in jbock 2.8.3.
 * Primitives and boxed primitives are omitted here.
 * All non-private enums can also be used.
 * For each such type X, Optional<X> defines an optional parameter,
 * and List<X> defines a repeatable parameter.
 * boolean or Boolean defines a flag.
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
   * Mapped by: java.io.File::new
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
