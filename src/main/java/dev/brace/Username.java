package dev.brace;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

import java.util.concurrent.Callable;

import static picocli.CommandLine.Spec;

@Command(name = "username", description = "Set the global username for all oliver functions")
public class Username implements Callable<Integer> {
    @Spec
    CommandSpec spec;

    @ParentCommand
    Oliver parent;

    @Parameters(index = "0", description = "the username to set")
    public void setUsername(String value) {
        if (Utilities.hasEmoji(value)) {
            username = value;
        } else throw new ParameterException(spec.commandLine(),
                String.format("Invalid value '%s' for option '--username': " +
                        "username does not contain an emoji", value));
    }

    private GlobalConfiguration config;
    private String username;

    @Override

    public Integer call() {
        this.config = new GlobalConfiguration(parent.globalConfigPath);

        config.setProperty("username", username);
        System.out.printf("Saved new username %s to global configuration at %s", username, config.getConfigFilePath().normalize());

        return 0;
    }
}
