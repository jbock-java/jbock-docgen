package com.example.helloworld;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EverythingTest {

    @Test
    void noInput() {
        Everything_Parser.ParseResult result = Everything_Parser.create()
                .withErrorStream(new PrintStream(new ByteArrayOutputStream()))
                .parse(new String[]{});
        assertTrue(result.error());
    }
}