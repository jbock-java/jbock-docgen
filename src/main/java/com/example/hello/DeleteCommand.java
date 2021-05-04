package com.example.hello;

import net.jbock.Command;
import net.jbock.Option;
import net.jbock.Parameter;

import java.nio.file.Path;
import java.util.OptionalInt;

@Command
abstract class DeleteCommand {

    /**
     * A positional parameter.
     */
    @Parameter(index = 0)
    abstract Path path();

    /**
     * A named option.
     */
    @Option(names = "--verbosity")
    abstract OptionalInt verbosity();
}
