package com.example.hello;

import net.jbock.CommandLineArguments;
import net.jbock.Parameter;
import net.jbock.PositionalParameter;

import java.nio.file.Path;
import java.util.OptionalInt;

@CommandLineArguments
abstract class MyArguments {

    @PositionalParameter(1)
    abstract Path path();

    @Parameter(value = "verbosity", mnemonic = 'v')
    abstract OptionalInt verbosity();
}
