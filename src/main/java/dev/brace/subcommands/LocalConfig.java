package dev.brace.subcommands;

import dev.brace.config.ConfigurationHandler;
import dev.brace.config.LocalConfigurationOption;
import dev.brace.repository.Repository;
import picocli.CommandLine.Model.ArgGroupSpec;
import picocli.CommandLine.Model.ArgGroupSpec.Builder;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Model.OptionSpec;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Callable;

import static picocli.CommandLine.*;

@Command(name = "local-config", description = "Set a local configuration option in the current repository.", mixinStandardHelpOptions = true, modelTransformer = LocalConfig.AddLocalConfigurationOptions.class)
public class LocalConfig implements Callable<Integer> {
    private static final String CONFIG_OPTIONS_GROUP_NAME = "Valid options%n";

    // Transform the CommandSpec to include all configuration options as CLI options
    static class AddLocalConfigurationOptions implements IModelTransformer {
        public CommandSpec transform(CommandSpec spec) {
            // Define a custom ArgGroup so that only config options are set, not all CLI options
            Builder groupBuilder = ArgGroupSpec.builder();
            groupBuilder.heading(CONFIG_OPTIONS_GROUP_NAME);
            groupBuilder.validate(false);

            // For all possible config values, defined in GlobalConfigurationOption
            for (LocalConfigurationOption configOptionValues : LocalConfigurationOption.values()) {
                OptionSpec option = OptionSpec.builder(String.format("--%s", configOptionValues.getName()))
                        .description(configOptionValues.getDescription())
                        .paramLabel(configOptionValues.getParamLabel())
                        .type(configOptionValues.getType()).build();

                // Add to the CLI spec and ArgGroup
                groupBuilder.addArg(option);
            }

            spec.addArgGroup(groupBuilder.build());

            return spec;
        }
    }

    @Spec
    CommandSpec spec;

    @ParentCommand
    Oliver parent;

    @Option(names = {"-r", "--repository"}, paramLabel = "PATH", description = "Set the path to the repository")
    private Path repository = Path.of(".");

    @Override
    public Integer call() throws IOException {
        if (!Repository.inRepository(repository))
            throw new IOException("The current or provided path is not a repository.");

        File configFile = Paths.get(repository.toString(), ".oliver", "config.properties").toFile();
        ConfigurationHandler configHandler = new ConfigurationHandler(configFile);

        // Extract dynamic options
        List<OptionSpec> options = spec.options().stream().filter(
                (OptionSpec option) -> option.group() != null && option.group().heading().equals(CONFIG_OPTIONS_GROUP_NAME)
        ).toList();

        boolean anyConfigUpdated = configHandler.setFromOptionList(options);

        System.out.println(anyConfigUpdated ? String.format("Local configuration updated at \u001B[36m%s\u001B[0m", configHandler.getConfigFilePath().normalize()) : "No config values were changed.");

        return 0;
    }
}
