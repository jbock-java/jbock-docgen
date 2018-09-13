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

@CommandLineArguments
abstract class JbockAllTypes {

  @Parameter
  abstract String stringArray();

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
  abstract List<BigDecimal> bigDecimalList();

  @Parameter
  abstract List<BigInteger> bigIntegerList();

  @Parameter
  abstract List<Character> characterList();

  @Parameter
  abstract List<Charset> charsetList();

  @Parameter
  abstract List<Double> doubleList();

  @Parameter
  abstract List<File> fileList();

  @Parameter
  abstract List<Float> floatList();

  @Parameter
  abstract List<Instant> instantList();

  @Parameter
  abstract List<Integer> integerList();

  @Parameter
  abstract List<LocalDate> localDateList();

  @Parameter
  abstract List<LocalDateTime> localDateTimeList();

  @Parameter
  abstract List<Long> longList();

  @Parameter
  abstract List<OffsetDateTime> offsetDateTimeList();

  @Parameter
  abstract List<Path> pathList();

  @Parameter
  abstract List<Pattern> patternList();

  @Parameter
  abstract List<String> stringList();

  @Parameter
  abstract List<URI> uriList();

  @Parameter
  abstract List<ZonedDateTime> zonedDateTimeList();

  @Parameter
  abstract Optional<BigDecimal> bigDecimalOpt();

  @Parameter
  abstract Optional<BigInteger> bigIntegerOpt();

  @Parameter
  abstract Optional<Character> characterOpt();

  @Parameter
  abstract Optional<Charset> charsetOpt();

  @Parameter
  abstract Optional<Double> doubleOpt();

  @Parameter
  abstract Optional<File> fileOpt();

  @Parameter
  abstract Optional<Float> floatOpt();

  @Parameter
  abstract Optional<Instant> instantOpt();

  @Parameter
  abstract Optional<Integer> integerOpt();

  @Parameter
  abstract Optional<LocalDate> localDateOpt();

  @Parameter
  abstract Optional<LocalDateTime> localDateTimeOpt();

  @Parameter
  abstract Optional<Long> longOpt();

  @Parameter
  abstract Optional<OffsetDateTime> offsetDateTimeOpt();

  @Parameter
  abstract Optional<Path> pathOpt();

  @Parameter
  abstract Optional<Pattern> patternOpt();

  @Parameter
  abstract Optional<String> stringOpt();

  @Parameter
  abstract Optional<URI> uriOpt();

  @Parameter
  abstract Optional<ZonedDateTime> zonedDateTimeOpt();
}
