package com.example.hello;

import net.jbock.CommandLineArguments;
import net.jbock.Parameter;
import net.jbock.PositionalParameter;

import java.nio.file.Path;
import java.util.OptionalInt;

@CommandLineArguments
abstract class MyArguments {

    @PositionalParameter
    abstract Path path();

    @Parameter(shortName = 'v')
    abstract OptionalInt verbosity();
}
