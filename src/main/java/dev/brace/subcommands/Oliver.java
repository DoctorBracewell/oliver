package dev.brace.subcommands;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.Option;

import java.io.File;
import java.nio.file.Paths;

@Command(
        name = "oliver",
        description = "omnicapable lightweight incremental version exporting routine",
        version = "1.0",
        mixinStandardHelpOptions = true,
        subcommands = {
                HelpCommand.class,
                GlobalConfig.class,
                LocalConfig.class,
                Init.class,
        }
)
public class Oliver {
    @Option(names = {"-cp", "--config-path"}, paramLabel = "FILE", description = "path to the global configuration file", scope = CommandLine.ScopeType.INHERIT)
    File globalConfigPath = Paths.get(System.getProperty("user.home"), ".oliver", "config.properties").toFile();
}
