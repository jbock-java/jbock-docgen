package com.example.hello;

import net.jbock.Command;
import net.jbock.Option;
import net.jbock.Parameter;

import java.nio.file.Path;
import java.util.OptionalInt;

@Command(name = "rm", description = "Coffee time!")
abstract class DeleteCommand {

    @Parameter(index = 0,
               description = "A positional parameter.")
    abstract Path path();

    @Option(names = {"-v", "--verbosity"}, 
            description = "A named option.")
    abstract OptionalInt verbosity();
}
