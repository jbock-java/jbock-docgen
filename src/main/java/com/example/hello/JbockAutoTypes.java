package com.example.hello;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.regex.Pattern;
import net.jbock.CommandLineArguments;
import net.jbock.Parameter;

/**
 * This class contains all the basic parameter types
 * that can be used without custom mappers or collectors in jbock 2.8.2.
 * Primitives and boxed primitives are omitted here.
 * All non-private enums can also be used.
 * For each such type X, Optional<X> defines an optional parameter,
 * and List<X> defines a repeatable parameter.
 * boolean or Boolean defines a flag.
 */
@CommandLineArguments
abstract class JbockAutoTypes {
  /**
   * mapped by: java.math.BigDecimal::new
   */
  @Parameter(
      longName = "bigDecimal"
  )
  abstract BigDecimal bigDecimal();

  /**
   * mapped by: java.math.BigInteger::new
   */
  @Parameter(
      longName = "bigInteger"
  )
  abstract BigInteger bigInteger();

  /**
   * mapped by: java.nio.charset.Charset::forName
   */
  @Parameter(
      longName = "charset"
  )
  abstract Charset charset();

  /**
   * mapped by: java.io.File::new
   */
  @Parameter(
      longName = "file"
  )
  abstract File file();

  /**
   * mapped by: java.time.Instant::parse
   */
  @Parameter(
      longName = "instant"
  )
  abstract Instant instant();

  /**
   * mapped by: java.time.LocalDate::parse
   */
  @Parameter(
      longName = "localDate"
  )
  abstract LocalDate localDate();

  /**
   * mapped by: java.time.LocalDateTime::parse
   */
  @Parameter(
      longName = "localDateTime"
  )
  abstract LocalDateTime localDateTime();

  /**
   * mapped by: java.time.OffsetDateTime::parse
   */
  @Parameter(
      longName = "offsetDateTime"
  )
  abstract OffsetDateTime offsetDateTime();

  /**
   * mapped by: java.nio.file.Paths::get
   */
  @Parameter(
      longName = "path"
  )
  abstract Path path();

  /**
   * mapped by: java.util.regex.Pattern::compile
   */
  @Parameter(
      longName = "pattern"
  )
  abstract Pattern pattern();

  /**
   * mapped by: java.util.function.Function.identity()
   */
  @Parameter(
      longName = "string"
  )
  abstract String string();

  /**
   * mapped by: java.net.URI::create
   */
  @Parameter(
      longName = "uRI"
  )
  abstract URI uRI();

  /**
   * mapped by: java.time.ZonedDateTime::parse
   */
  @Parameter(
      longName = "zonedDateTime"
  )
  abstract ZonedDateTime zonedDateTime();
}
