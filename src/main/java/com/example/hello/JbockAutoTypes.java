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
 * that can be used without a custom mapper in jbock 2.8.2.
 * Optional and List thereof can also be used.
 * All non-private enums can also be used directly.
 * The default mapper will use their {@code static valueOf(String)} method.
 */
@CommandLineArguments
abstract class JbockAutoTypes {
  @Parameter(
      longName = "java.lang.Byte"
  )
  abstract Byte boxedByte();

  @Parameter(
      longName = "java.lang.Character"
  )
  abstract Character boxedCharacter();

  @Parameter(
      longName = "java.lang.Double"
  )
  abstract Double boxedDouble();

  @Parameter(
      longName = "java.lang.Float"
  )
  abstract Float boxedFloat();

  @Parameter(
      longName = "java.lang.Integer"
  )
  abstract Integer boxedInteger();

  @Parameter(
      longName = "java.lang.Long"
  )
  abstract Long boxedLong();

  @Parameter(
      longName = "java.lang.Short"
  )
  abstract Short boxedShort();

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
