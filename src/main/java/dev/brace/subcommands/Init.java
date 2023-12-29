package dev.brace.subcommands;

import dev.brace.repository.Repository;
import picocli.CommandLine.Model.CommandSpec;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

import static picocli.CommandLine.*;

@Command(name = "init", description = "Initialises a new repository", mixinStandardHelpOptions = true)
public class Init implements Callable<Integer> {

    @Spec
    CommandSpec spec;

    @ParentCommand
    Oliver parent;

    @Option(names = {"-r", "--repository"}, paramLabel = "PATH", description = "The folder where the repository should be initialised.")
    private Path repository;

    @Option(names = {"-n", "--name"}, required = true, paramLabel = "STRING", description = "The repository name.")
    private String name;

    @Override
    public Integer call() throws IOException {
        Path path = this.repository == null ? Paths.get(".", name) : repository;
        Path configPath = Paths.get(path.toString(), ".oliver", "config.properties");

        if (Repository.inRepository(path))
            throw new IOException("The current or provided path is already a repository.");

        String value = System.console().readLine("Repository will be created at \u001B[36m%s\u001B[0m, continue? (Y/n):", path);

        if (value.equals("n")) {
            System.out.println("Aborting...");
            return -1;
        } else {
            System.out.print("\n");
        }

        Repository.init(configPath.toFile(), name);

        return 0;
    }
}
