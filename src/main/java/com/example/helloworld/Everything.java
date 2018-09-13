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
abstract class Everything {
  @Parameter
  abstract String stringArray();

  @Parameter
  abstract boolean booleanPrimitive();

  @Parameter
  abstract char charPrimitive();

  @Parameter
  abstract double doublePrimitive();

  @Parameter
  abstract float floatPrimitive();

  @Parameter
  abstract int intPrimitive();

  @Parameter
  abstract long longPrimitive();

  @Parameter
  abstract BigDecimal BigDecimalRequired();

  @Parameter
  abstract BigInteger BigIntegerRequired();

  @Parameter
  abstract Boolean BooleanRequired();

  @Parameter
  abstract Character CharacterRequired();

  @Parameter
  abstract Charset CharsetRequired();

  @Parameter
  abstract Double DoubleRequired();

  @Parameter
  abstract File FileRequired();

  @Parameter
  abstract Float FloatRequired();

  @Parameter
  abstract Instant InstantRequired();

  @Parameter
  abstract Integer IntegerRequired();

  @Parameter
  abstract LocalDate LocalDateRequired();

  @Parameter
  abstract LocalDateTime LocalDateTimeRequired();

  @Parameter
  abstract Long LongRequired();

  @Parameter
  abstract OffsetDateTime OffsetDateTimeRequired();

  @Parameter
  abstract OptionalDouble OptionalDoubleRequired();

  @Parameter
  abstract OptionalInt OptionalIntRequired();

  @Parameter
  abstract OptionalLong OptionalLongRequired();

  @Parameter
  abstract Path PathRequired();

  @Parameter
  abstract Pattern PatternRequired();

  @Parameter
  abstract String StringRequired();

  @Parameter
  abstract URI URIRequired();

  @Parameter
  abstract ZonedDateTime ZonedDateTimeRequired();

  @Parameter
  abstract List<BigDecimal> BigDecimalList();

  @Parameter
  abstract List<BigInteger> BigIntegerList();

  @Parameter
  abstract List<Character> CharacterList();

  @Parameter
  abstract List<Charset> CharsetList();

  @Parameter
  abstract List<Double> DoubleList();

  @Parameter
  abstract List<File> FileList();

  @Parameter
  abstract List<Float> FloatList();

  @Parameter
  abstract List<Instant> InstantList();

  @Parameter
  abstract List<Integer> IntegerList();

  @Parameter
  abstract List<LocalDate> LocalDateList();

  @Parameter
  abstract List<LocalDateTime> LocalDateTimeList();

  @Parameter
  abstract List<Long> LongList();

  @Parameter
  abstract List<OffsetDateTime> OffsetDateTimeList();

  @Parameter
  abstract List<Path> PathList();

  @Parameter
  abstract List<Pattern> PatternList();

  @Parameter
  abstract List<String> StringList();

  @Parameter
  abstract List<URI> URIList();

  @Parameter
  abstract List<ZonedDateTime> ZonedDateTimeList();

  @Parameter
  abstract Optional<BigDecimal> BigDecimalOpt();

  @Parameter
  abstract Optional<BigInteger> BigIntegerOpt();

  @Parameter
  abstract Optional<Character> CharacterOpt();

  @Parameter
  abstract Optional<Charset> CharsetOpt();

  @Parameter
  abstract Optional<Double> DoubleOpt();

  @Parameter
  abstract Optional<File> FileOpt();

  @Parameter
  abstract Optional<Float> FloatOpt();

  @Parameter
  abstract Optional<Instant> InstantOpt();

  @Parameter
  abstract Optional<Integer> IntegerOpt();

  @Parameter
  abstract Optional<LocalDate> LocalDateOpt();

  @Parameter
  abstract Optional<LocalDateTime> LocalDateTimeOpt();

  @Parameter
  abstract Optional<Long> LongOpt();

  @Parameter
  abstract Optional<OffsetDateTime> OffsetDateTimeOpt();

  @Parameter
  abstract Optional<Path> PathOpt();

  @Parameter
  abstract Optional<Pattern> PatternOpt();

  @Parameter
  abstract Optional<String> StringOpt();

  @Parameter
  abstract Optional<URI> URIOpt();

  @Parameter
  abstract Optional<ZonedDateTime> ZonedDateTimeOpt();
}
