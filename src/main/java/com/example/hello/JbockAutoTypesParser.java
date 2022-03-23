package com.example.hello;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.regex.Pattern;
import javax.annotation.processing.Generated;

@Generated(
    value = "net.jbock.processor.JbockProcessor",
    comments = "https://github.com/jbock-java/jbock"
)
final class JbockAutoTypes_Impl extends JbockAutoTypes {
  private final File file;

  private final String string;

  private final BigDecimal bigDecimal;

  private final BigInteger bigInteger;

  private final URI uRI;

  private final Path path;

  private final LocalDate localDate;

  private final Pattern pattern;

  JbockAutoTypes_Impl(File file, String string, BigDecimal bigDecimal, BigInteger bigInteger,
      URI uRI, Path path, LocalDate localDate, Pattern pattern) {
    this.file = file;
    this.string = string;
    this.bigDecimal = bigDecimal;
    this.bigInteger = bigInteger;
    this.uRI = uRI;
    this.path = path;
    this.localDate = localDate;
    this.pattern = pattern;
  }

  @Override
  File file() {
    return file;
  }

  @Override
  String string() {
    return string;
  }

  @Override
  BigDecimal bigDecimal() {
    return bigDecimal;
  }

  @Override
  BigInteger bigInteger() {
    return bigInteger;
  }

  @Override
  URI uRI() {
    return uRI;
  }

  @Override
  Path path() {
    return path;
  }

  @Override
  LocalDate localDate() {
    return localDate;
  }

  @Override
  Pattern pattern() {
    return pattern;
  }
}
