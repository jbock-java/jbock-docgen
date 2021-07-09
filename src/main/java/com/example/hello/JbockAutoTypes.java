package com.example.hello;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.regex.Pattern;
import javax.annotation.processing.Generated;
import net.jbock.Command;
import net.jbock.Option;

/**
 * This class contains all "auto types"
 * that can be used without a custom converter in jbock 5.4:
 *
 * <ul>
 *   <li>{@code java.io.File}
 *   <li>{@code java.lang.String}
 *   <li>{@code java.math.BigDecimal}
 *   <li>{@code java.math.BigInteger}
 *   <li>{@code java.net.URI}
 *   <li>{@code java.nio.file.Path}
 *   <li>{@code java.time.LocalDate}
 *   <li>{@code java.util.regex.Pattern}
 * </ul>
 *
 * Primitives and boxed primitives are also auto types, except the booleans.
 * All enums are auto types. They are converted via their static {@code valueOf} method.
 * Special rules apply for these types:
 *
 * <ul>
 *   <li>{@code boolean}
 *   <li>{@code java.util.List}
 *   <li>{@code java.util.Optional}
 *   <li>{@code java.util.OptionalInt}
 *   <li>{@code java.util.OptionalLong}
 *   <li>{@code java.util.OptionalDouble}
 *   <li>{@code io.vavr.control.Option}
 * </ul>
 */
@Generated("net.jbock.convert.GenAutoTypes")
@Command
abstract class JbockAutoTypes {
  /**
   * converter: java.io.File file = new java.io.File(token);
   * if (!file.exists())
   *   throw new java.lang.IllegalStateException("File does not exist: " + token);
   * if (!file.isFile())
   *   throw new java.lang.IllegalStateException("Not a file: " + token);
   * return file;
   *
   */
  @Option(
      names = "--file"
  )
  abstract File file();

  /**
   * converter: net.jbock.util.StringConverter.create(java.util.function.Function.identity())
   */
  @Option(
      names = "--string"
  )
  abstract String string();

  /**
   * converter: net.jbock.util.StringConverter.create(java.math.BigDecimal::new)
   */
  @Option(
      names = "--bigdecimal"
  )
  abstract BigDecimal bigDecimal();

  /**
   * converter: net.jbock.util.StringConverter.create(java.math.BigInteger::new)
   */
  @Option(
      names = "--biginteger"
  )
  abstract BigInteger bigInteger();

  /**
   * converter: net.jbock.util.StringConverter.create(java.net.URI::create)
   */
  @Option(
      names = "--uri"
  )
  abstract URI uRI();

  /**
   * converter: net.jbock.util.StringConverter.create(java.nio.file.Paths::get)
   */
  @Option(
      names = "--path"
  )
  abstract Path path();

  /**
   * converter: net.jbock.util.StringConverter.create(java.time.LocalDate::parse)
   */
  @Option(
      names = "--localdate"
  )
  abstract LocalDate localDate();

  /**
   * converter: net.jbock.util.StringConverter.create(java.util.regex.Pattern::compile)
   */
  @Option(
      names = "--pattern"
  )
  abstract Pattern pattern();
}
