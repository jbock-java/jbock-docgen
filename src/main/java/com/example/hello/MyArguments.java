package com.example.hello;

import net.jbock.Command;
import net.jbock.Option;
import net.jbock.Param;

import java.nio.file.Path;
import java.util.OptionalInt;

@Command
abstract class MyArguments {

    /**
     * A "param" is a positional parameter.
     */
    @Param(1)
    abstract Path path();

    /**
     * This javadoc will show up when "--help" is passed.
     * Alternatively you can define the help text in a resource bundle.
     */
    @Option(value = "verbosity", mnemonic = 'v')
    abstract OptionalInt verbosity();
}
