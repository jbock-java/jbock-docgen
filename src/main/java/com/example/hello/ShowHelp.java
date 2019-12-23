package com.example.hello;

public class ShowHelp {

    public static void main(String[] args) {
        new MyArguments_Parser().parseOrExit(new String[]{"--help"});
    }
}
