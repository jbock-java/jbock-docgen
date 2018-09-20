package com.example.helloworld;

import java.io.File;
import java.lang.Boolean;
import java.lang.Character;
import java.lang.Double;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Long;
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
 * Lists represent repeatable arguments.
 * Optionals represent optional arguments.
 * Booleans represent flags, unless there's a mapper.
 * Everything else represents a required argument.
 */
@CommandLineArguments
abstract class JbockAllTypes {

  @Parameter
  abstract boolean requiredBooleanPrimitive();

  @Parameter
  abstract char requiredCharPrimitive();

  @Parameter
  abstract double requiredDoublePrimitive();

  @Parameter
  abstract float requiredFloatPrimitive();

  @Parameter
  abstract int requiredIntPrimitive();

  @Parameter
  abstract long requiredLongPrimitive();

  @Parameter
  abstract OptionalDouble optionalDouble();

  @Parameter
  abstract OptionalInt optionalInt();

  @Parameter
  abstract OptionalLong optionalLong();

  @Parameter
  abstract BigDecimal requiredBigDecimal();

  @Parameter
  abstract BigInteger requiredBigInteger();

  @Parameter
  abstract Boolean requiredBoolean();

  @Parameter
  abstract Character requiredCharacter();

  @Parameter
  abstract Charset requiredCharset();

  @Parameter
  abstract Double requiredDouble();

  @Parameter
  abstract File requiredFile();

  @Parameter
  abstract Float requiredFloat();

  @Parameter
  abstract Instant requiredInstant();

  @Parameter
  abstract Integer requiredInteger();

  @Parameter
  abstract LocalDate requiredLocalDate();

  @Parameter
  abstract LocalDateTime requiredLocalDateTime();

  @Parameter
  abstract Long requiredLong();

  @Parameter
  abstract OffsetDateTime requiredOffsetDateTime();

  @Parameter
  abstract Path requiredPath();

  @Parameter
  abstract Pattern requiredPattern();

  @Parameter
  abstract String requiredString();

  @Parameter
  abstract URI requiredUri();

  @Parameter
  abstract ZonedDateTime requiredZonedDateTime();
}
