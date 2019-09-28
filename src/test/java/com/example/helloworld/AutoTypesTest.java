package com.example.helloworld;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AutoTypesTest {

    @Test
    void noInput() {
        AutoTypes_Parser.ParseResult result = AutoTypes_Parser.create()
                .withErrorStream(new PrintStream(new ByteArrayOutputStream()))
                .parse(new String[]{});
        assertTrue(result instanceof AutoTypes_Parser.ParsingFailed);
    }
}
