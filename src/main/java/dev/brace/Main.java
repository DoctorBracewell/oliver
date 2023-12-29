package dev.brace;

import dev.brace.subcommands.Oliver;
import picocli.CommandLine;

public class Main {
    public static void main(String... args) {
        int exitCode = new CommandLine(new Oliver()).execute(args);
        System.exit(exitCode);
    }
}
