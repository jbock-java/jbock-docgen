package com.example.hello;

public class ShowHelp {

    public static void main(String[] args) {
        MyArguments_Parser.create().parseOrExit(new String[]{"--help"});
    }
}
