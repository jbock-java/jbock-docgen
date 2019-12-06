package com.example.hello;

import net.jbock.Command;
import net.jbock.Option;
import net.jbock.Param;

import java.nio.file.Path;
import java.util.OptionalInt;

@Command
abstract class MyArguments {

    @Param(1)
    abstract Path path();

    @Option(value = "verbosity", mnemonic = 'v')
    abstract OptionalInt verbosity();
}
