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
 * <p>This class contains all "auto types"
 * that can be used without a custom converter in jbock 5.1:</p>
 *
 * <ul>
 *   <li>java.io.File</li>
 *   <li>java.lang.String</li>
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
