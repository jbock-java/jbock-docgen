package com.example.hello;

import net.jbock.Command;
import net.jbock.Option;
import net.jbock.Param;

import java.nio.file.Path;
import java.util.OptionalInt;

@Command
abstract class MyCommand {

    /**
     * A positional parameter.
     */
    @Param(0)
    abstract Path path();

    /**
     * A named option.
     */
    @Option("verbosity")
    abstract OptionalInt verbosity();
}
