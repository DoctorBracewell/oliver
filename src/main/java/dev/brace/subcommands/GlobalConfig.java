package dev.brace.subcommands;

import dev.brace.config.ConfigurationHandler;
import dev.brace.config.GlobalConfigurationOption;
import picocli.CommandLine.Model.ArgGroupSpec;
import picocli.CommandLine.Model.ArgGroupSpec.Builder;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Model.OptionSpec;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import static picocli.CommandLine.*;

@Command(name = "global-config", description = "Set a global configuration option", mixinStandardHelpOptions = true, modelTransformer = GlobalConfig.AddGlobalConfigurationOptions.class)
public class GlobalConfig implements Callable<Integer> {
    private static final String CONFIG_OPTIONS_GROUP_NAME = "Valid options%n";

    // Transform the CommandSpec to include all configuration options as CLI options
    static class AddGlobalConfigurationOptions implements IModelTransformer {
        public CommandSpec transform(CommandSpec spec) {
            // Define a custom ArgGroup so that only config options are set, not all CLI options
            Builder groupBuilder = ArgGroupSpec.builder();
            groupBuilder.heading(CONFIG_OPTIONS_GROUP_NAME);
            groupBuilder.validate(false);

            // For all possible config values, defined in GlobalConfigurationOption
            for (GlobalConfigurationOption configOptionValues : GlobalConfigurationOption.values()) {
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

    @Override
    public Integer call() throws IOException {
        ConfigurationHandler configHandler = new ConfigurationHandler(parent.globalConfigPath);

        // Extract dynamic options
        List<OptionSpec> options = spec.options().stream().filter(
                (OptionSpec option) -> option.group() != null && option.group().heading().equals(CONFIG_OPTIONS_GROUP_NAME)
        ).toList();

        boolean anyConfigUpdated = configHandler.setFromOptionList(options);
        System.out.println(anyConfigUpdated ? String.format("Global configuration updated at \u001B[36m%s\u001B[0m", configHandler.getConfigFilePath().normalize()) : "No config values were changed.");

        return 0;
    }
}
