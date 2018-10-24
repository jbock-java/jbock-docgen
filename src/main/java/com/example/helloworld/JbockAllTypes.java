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

@CommandLineArguments
abstract class JbockAllTypes {
  @Parameter(
      longName = "booleanPrimitive",
      flag = true
  )
  abstract boolean booleanPrimitive();

  @Parameter(
      longName = "bytePrimitive"
  )
  abstract byte bytePrimitive();

  @Parameter(
      longName = "charPrimitive"
  )
  abstract char charPrimitive();

  @Parameter(
      longName = "doublePrimitive"
  )
  abstract double doublePrimitive();

  @Parameter(
      longName = "floatPrimitive"
  )
  abstract float floatPrimitive();

  @Parameter(
      longName = "intPrimitive"
  )
  abstract int intPrimitive();

  @Parameter(
      longName = "longPrimitive"
  )
  abstract long longPrimitive();

  @Parameter(
      longName = "shortPrimitive"
  )
  abstract short shortPrimitive();

  @Parameter(
      longName = "optionalDouble",
      optional = true
  )
  abstract OptionalDouble optionalDouble();

  @Parameter(
      longName = "optionalInt",
      optional = true
  )
  abstract OptionalInt optionalInt();

  @Parameter(
      longName = "optionalLong",
      optional = true
  )
  abstract OptionalLong optionalLong();

  @Parameter(
      longName = "requiredBigDecimal"
  )
  abstract BigDecimal requiredBigDecimal();

  @Parameter(
      longName = "requiredBigInteger"
  )
  abstract BigInteger requiredBigInteger();

  @Parameter(
      longName = "requiredBoolean",
      flag = true
  )
  abstract Boolean requiredBoolean();

  @Parameter(
      longName = "requiredByte"
  )
  abstract Byte requiredByte();

  @Parameter(
      longName = "requiredCharacter"
  )
  abstract Character requiredCharacter();

  @Parameter(
      longName = "requiredCharset"
  )
  abstract Charset requiredCharset();

  @Parameter(
      longName = "requiredDouble"
  )
  abstract Double requiredDouble();

  @Parameter(
      longName = "requiredFile"
  )
  abstract File requiredFile();

  @Parameter(
      longName = "requiredFloat"
  )
  abstract Float requiredFloat();

  @Parameter(
      longName = "requiredInstant"
  )
  abstract Instant requiredInstant();

  @Parameter(
      longName = "requiredInteger"
  )
  abstract Integer requiredInteger();

  @Parameter(
      longName = "requiredLocalDate"
  )
  abstract LocalDate requiredLocalDate();

  @Parameter(
      longName = "requiredLocalDateTime"
  )
  abstract LocalDateTime requiredLocalDateTime();

  @Parameter(
      longName = "requiredLong"
  )
  abstract Long requiredLong();

  @Parameter(
      longName = "requiredOffsetDateTime"
  )
  abstract OffsetDateTime requiredOffsetDateTime();

  @Parameter(
      longName = "requiredPath"
  )
  abstract Path requiredPath();

  @Parameter(
      longName = "requiredPattern"
  )
  abstract Pattern requiredPattern();

  @Parameter(
      longName = "requiredShort"
  )
  abstract Short requiredShort();

  @Parameter(
      longName = "requiredString"
  )
  abstract String requiredString();

  @Parameter(
      longName = "requiredUri"
  )
  abstract URI requiredUri();

  @Parameter(
      longName = "requiredZonedDateTime"
  )
  abstract ZonedDateTime requiredZonedDateTime();
}
