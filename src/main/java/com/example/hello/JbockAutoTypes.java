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
  @Parameter(
      longName = "java.math.BigDecimal"
  )
  abstract BigDecimal BigDecimal();

  @Parameter(
      longName = "java.math.BigInteger"
  )
  abstract BigInteger BigInteger();

  @Parameter(
      longName = "java.nio.charset.Charset"
  )
  abstract Charset Charset();

  @Parameter(
      longName = "java.io.File"
  )
  abstract File File();

  @Parameter(
      longName = "java.time.Instant"
  )
  abstract Instant Instant();

  @Parameter(
      longName = "java.time.LocalDate"
  )
  abstract LocalDate LocalDate();

  @Parameter(
      longName = "java.time.LocalDateTime"
  )
  abstract LocalDateTime LocalDateTime();

  @Parameter(
      longName = "java.time.OffsetDateTime"
  )
  abstract OffsetDateTime OffsetDateTime();

  @Parameter(
      longName = "java.nio.file.Path"
  )
  abstract Path Path();

  @Parameter(
      longName = "java.util.regex.Pattern"
  )
  abstract Pattern Pattern();

  @Parameter(
      longName = "java.lang.String"
  )
  abstract String String();

  @Parameter(
      longName = "java.net.URI"
  )
  abstract URI URI();

  @Parameter(
      longName = "java.time.ZonedDateTime"
  )
  abstract ZonedDateTime ZonedDateTime();
}
