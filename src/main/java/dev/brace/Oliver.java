package dev.brace;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.nio.file.Paths;

@Command(
        name = "oliver",
        description = "omnicapable lightweight version exporting routine",
        version = "1.0",
        mixinStandardHelpOptions = true,
        subcommands = {
                Username.class
        }
)
class Oliver {
    @Option(names = {"-gc", "--global-config"}, paramLabel = "FILE", description = "path to the global configuration file", scope = CommandLine.ScopeType.INHERIT)
    File globalConfigPath = new File(Paths.get(System.getProperty("user.home"), ".oliver", "config.properties").toString());

    // this example implements Callable, so parsing, error handling and handling user
    // requests for usage help or version help can be done with one line of code.
    public static void main(String... args) {
        int exitCode = new CommandLine(new Oliver()).execute(args);
        System.exit(exitCode);
    }
}
