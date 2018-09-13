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
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.regex.Pattern;

import net.jbock.CommandLineArguments;
import net.jbock.Parameter;

/**
 * Lists and arrays represent repeatable arguments.
 * Optionals represent optional arguments.
 * Booleans represent flags.
 * Everything else represents a required argument.
 */
@CommandLineArguments
abstract class JbockAllTypes {

  @Parameter
  abstract String[] stringArray();

  @Parameter
  abstract boolean booleanPrimitiveRequired();

  @Parameter
  abstract char charPrimitiveRequired();

  @Parameter
  abstract double doublePrimitiveRequired();

  @Parameter
  abstract float floatPrimitiveRequired();

  @Parameter
  abstract int intPrimitiveRequired();

  @Parameter
  abstract long longPrimitiveRequired();

  @Parameter
  abstract BigDecimal bigDecimalRequired();

  @Parameter
  abstract BigInteger bigIntegerRequired();

  @Parameter
  abstract Boolean booleanRequired();

  @Parameter
  abstract Character characterRequired();

  @Parameter
  abstract Charset charsetRequired();

  @Parameter
  abstract Double doubleRequired();

  @Parameter
  abstract File fileRequired();

  @Parameter
  abstract Float floatRequired();

  @Parameter
  abstract Instant instantRequired();

  @Parameter
  abstract Integer integerRequired();

  @Parameter
  abstract LocalDate localDateRequired();

  @Parameter
  abstract LocalDateTime localDateTimeRequired();

  @Parameter
  abstract Long longRequired();

  @Parameter
  abstract OffsetDateTime offsetDateTimeRequired();

  @Parameter
  abstract OptionalDouble optionalDouble();

  @Parameter
  abstract OptionalInt optionalInt();

  @Parameter
  abstract OptionalLong optionalLong();

  @Parameter
  abstract Path pathRequired();

  @Parameter
  abstract Pattern patternRequired();

  @Parameter
  abstract String stringRequired();

  @Parameter
  abstract URI uriRequired();

  @Parameter
  abstract ZonedDateTime zonedDateTimeRequired();

  @Parameter
  abstract List<BigDecimal> bigDecimalRepeatable();

  @Parameter
  abstract List<BigInteger> bigIntegerRepeatable();

  @Parameter
  abstract List<Character> characterRepeatable();

  @Parameter
  abstract List<Charset> charsetRepeatable();

  @Parameter
  abstract List<Double> doubleRepeatable();

  @Parameter
  abstract List<File> fileRepeatable();

  @Parameter
  abstract List<Float> floatRepeatable();

  @Parameter
  abstract List<Instant> instantRepeatable();

  @Parameter
  abstract List<Integer> integerRepeatable();

  @Parameter
  abstract List<LocalDate> localDateRepeatable();

  @Parameter
  abstract List<LocalDateTime> localDateTimeRepeatable();

  @Parameter
  abstract List<Long> longRepeatable();

  @Parameter
  abstract List<OffsetDateTime> offsetDateTimeRepeatable();

  @Parameter
  abstract List<Path> pathRepeatable();

  @Parameter
  abstract List<Pattern> patternRepeatable();

  @Parameter
  abstract List<String> stringRepeatable();

  @Parameter
  abstract List<URI> uriRepeatable();

  @Parameter
  abstract List<ZonedDateTime> zonedDateTimeRepeatable();

  @Parameter
  abstract Optional<BigDecimal> bigDecimalOptional();

  @Parameter
  abstract Optional<BigInteger> bigIntegerOptional();

  @Parameter
  abstract Optional<Character> characterOptional();

  @Parameter
  abstract Optional<Charset> charsetOptional();

  @Parameter
  abstract Optional<Double> doubleOptional();

  @Parameter
  abstract Optional<File> fileOptional();

  @Parameter
  abstract Optional<Float> floatOptional();

  @Parameter
  abstract Optional<Instant> instantOptional();

  @Parameter
  abstract Optional<Integer> integerOptional();

  @Parameter
  abstract Optional<LocalDate> localDateOptional();

  @Parameter
  abstract Optional<LocalDateTime> localDateTimeOptional();

  @Parameter
  abstract Optional<Long> longOptional();

  @Parameter
  abstract Optional<OffsetDateTime> offsetDateTimeOptional();

  @Parameter
  abstract Optional<Path> pathOptional();

  @Parameter
  abstract Optional<Pattern> patternOptional();

  @Parameter
  abstract Optional<String> stringOptional();

  @Parameter
  abstract Optional<URI> uriOptional();

  @Parameter
  abstract Optional<ZonedDateTime> zonedDateTimeOptional();
}
