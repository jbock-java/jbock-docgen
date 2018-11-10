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
          longName = "boolean",
          flag = true
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
          longName = "Boolean",
          flag = true
  )
  abstract Boolean aBoxedBoolean();

  @Parameter(
          longName = "Byte"
  )
  abstract Byte aBoxedByte();

  @Parameter(
          longName = "Character"
  )
  abstract Character aBoxedCharacter();

  @Parameter(
          longName = "Double"
  )
  abstract Double aBoxedDouble();

  @Parameter(
          longName = "Float"
  )
  abstract Float aBoxedFloat();

  @Parameter(
          longName = "Integer"
  )
  abstract Integer aBoxedInteger();

  @Parameter(
          longName = "Long"
  )
  abstract Long aBoxedLong();

  @Parameter(
          longName = "Short"
  )
  abstract Short aBoxedShort();

  @Parameter(
          longName = "OptionalDouble",
          optional = true
  )
  abstract OptionalDouble aOptionalDouble();

  @Parameter(
          longName = "OptionalInt",
          optional = true
  )
  abstract OptionalInt aOptionalInt();

  @Parameter(
          longName = "OptionalLong",
          optional = true
  )
  abstract OptionalLong aOptionalLong();

  @Parameter(
          longName = "URI"
  )
  abstract URI aURI();

  @Parameter(
          longName = "Instant"
  )
  abstract Instant aInstant();

  @Parameter(
          longName = "LocalDate"
  )
  abstract LocalDate aLocalDate();

  @Parameter(
          longName = "LocalDateTime"
  )
  abstract LocalDateTime aLocalDateTime();

  @Parameter(
          longName = "OffsetDateTime"
  )
  abstract OffsetDateTime aOffsetDateTime();

  @Parameter(
          longName = "ZonedDateTime"
  )
  abstract ZonedDateTime aZonedDateTime();

  @Parameter(
          longName = "BigDecimal"
  )
  abstract BigDecimal aBigDecimal();

  @Parameter(
          longName = "BigInteger"
  )
  abstract BigInteger aBigInteger();

  @Parameter(
          longName = "String"
  )
  abstract String aString();

  @Parameter(
          longName = "Charset"
  )
  abstract Charset aCharset();

  @Parameter(
          longName = "Path"
  )
  abstract Path aPath();

  @Parameter(
          longName = "File"
  )
  abstract File aFile();

  @Parameter(
          longName = "Pattern"
  )
  abstract Pattern aPattern();
}
