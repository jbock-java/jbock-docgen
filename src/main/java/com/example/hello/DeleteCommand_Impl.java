package com.example.hello;

import java.nio.file.Path;
import java.util.OptionalInt;
import javax.annotation.processing.Generated;

@Generated(
    value = "net.jbock.processor.JbockProcessor",
    comments = "https://github.com/jbock-java"
)
final class DeleteCommand_Impl extends DeleteCommand {
  private final OptionalInt verbosity;

  private final Path path;

  DeleteCommand_Impl(OptionalInt verbosity, Path path) {
    this.verbosity = verbosity;
    this.path = path;
  }

  @Override
  OptionalInt verbosity() {
    return verbosity;
  }

  @Override
  Path path() {
    return path;
  }
}
