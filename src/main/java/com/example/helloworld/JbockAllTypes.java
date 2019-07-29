package com.example.helloworld;

import java.io.File;
import java.lang.Boolean;
import java.lang.Byte;
import java.lang.Character;
import java.lang.Double;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Short;
import java.lang.String;
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
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.regex.Pattern;
import net.jbock.CommandLineArguments;
import net.jbock.Parameter;

/**
 * This class contains all the basic parameter types
 * that can be used without a custom mapper in jbock 2.7.2.
 * All non-private enums can also be used directly.
 * The default behaviour is to use their {@code static valueOf(String)} method.
 */
@CommandLineArguments
abstract class JbockAllTypes {
  @Parameter(
      longName = "boolean"
  )
  abstract boolean aboolean();

  @Parameter(
      longName = "byte"
  )
  abstract byte abyte();

  @Parameter(
      longName = "char"
  )
  abstract char achar();

  @Parameter(
      longName = "double"
  )
  abstract double adouble();

  @Parameter(
      longName = "float"
  )
  abstract float afloat();

  @Parameter(
      longName = "int"
  )
  abstract int aint();

  @Parameter(
      longName = "long"
  )
  abstract long along();

  @Parameter(
      longName = "short"
  )
  abstract short ashort();

  @Parameter(
      longName = "java.lang.Boolean"
  )
  abstract Boolean aBoxedBoolean();

  @Parameter(
      longName = "java.lang.Byte"
  )
  abstract Byte aBoxedByte();

  @Parameter(
      longName = "java.lang.Character"
  )
  abstract Character aBoxedCharacter();

  @Parameter(
      longName = "java.lang.Double"
  )
  abstract Double aBoxedDouble();

  @Parameter(
      longName = "java.lang.Float"
  )
  abstract Float aBoxedFloat();

  @Parameter(
      longName = "java.lang.Integer"
  )
  abstract Integer aBoxedInteger();

  @Parameter(
      longName = "java.lang.Long"
  )
  abstract Long aBoxedLong();

  @Parameter(
      longName = "java.lang.Short"
  )
  abstract Short aBoxedShort();

  @Parameter(
      longName = "java.util.OptionalDouble"
  )
  abstract OptionalDouble aOptionalDouble();

  @Parameter(
      longName = "java.util.OptionalInt"
  )
  abstract OptionalInt aOptionalInt();

  @Parameter(
      longName = "java.util.OptionalLong"
  )
  abstract OptionalLong aOptionalLong();

  @Parameter(
      longName = "java.net.URI"
  )
  abstract URI aURI();

  @Parameter(
      longName = "java.time.Instant"
  )
  abstract Instant aInstant();

  @Parameter(
      longName = "java.time.LocalDate"
  )
  abstract LocalDate aLocalDate();

  @Parameter(
      longName = "java.time.LocalDateTime"
  )
  abstract LocalDateTime aLocalDateTime();

  @Parameter(
      longName = "java.time.OffsetDateTime"
  )
  abstract OffsetDateTime aOffsetDateTime();

  @Parameter(
      longName = "java.time.ZonedDateTime"
  )
  abstract ZonedDateTime aZonedDateTime();

  @Parameter(
      longName = "java.math.BigDecimal"
  )
  abstract BigDecimal aBigDecimal();

  @Parameter(
      longName = "java.math.BigInteger"
  )
  abstract BigInteger aBigInteger();

  @Parameter(
      longName = "java.lang.String"
  )
  abstract String aString();

  @Parameter(
      longName = "java.nio.charset.Charset"
  )
  abstract Charset aCharset();

  @Parameter(
      longName = "java.nio.file.Path"
  )
  abstract Path aPath();

  @Parameter(
      longName = "java.io.File"
  )
  abstract File aFile();

  @Parameter(
      longName = "java.util.regex.Pattern"
  )
  abstract Pattern aPattern();
}
