package com.example.hello;

import net.jbock.Command;
import net.jbock.Option;
import net.jbock.Param;

import java.nio.file.Path;
import java.util.OptionalInt;

@Command
abstract class MyCommand {

    /**
     * A {@code @Param} is a positional parameter.
     * This particular param is in the first position,
     * since there are no other params in lower positions.
     */
    @Param(0)
    abstract Path path();

    /**
     * An {@code @Option} is a named option.
     */
    @Option("verbosity")
    abstract OptionalInt verbosity();
}
