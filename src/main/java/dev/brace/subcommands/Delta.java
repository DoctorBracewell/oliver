package dev.brace.subcommands;

import com.networknt.schema.ValidationMessage;
import dev.brace.repository.Repository;
import dev.brace.repository.RepositoryDelta;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(name = "delta", description = "Creates a new delta from the changed files.", mixinStandardHelpOptions = true)
public class Delta implements Callable<Integer> {
    @Option(required = true, names = {"-f", "--file"}, paramLabel = "FILE", description = "path to the delta JSON file")
    File deltaFile;

    @Override
    public Integer call() throws IOException {
        if (!Repository.inRepository(Path.of(".")))
            throw new IOException("You are not in a repository");

        if (!deltaFile.getParentFile().toPath().endsWith(Path.of(".oliver", "deltas")))
            throw new IOException("The delta file must exist in the .oliver/deltas folder");

        RepositoryDelta delta = new RepositoryDelta(deltaFile, new Repository(Path.of(".")), deltaFile.getName());

        Set<ValidationMessage> errors = delta.validateFile();

        if (!errors.isEmpty()) { // "• \u001B[36m"
            String errorMessage = errors.stream().map(s -> {
                String str = s.toString();
                String[] m = str.split(":");
                return "• \u001B[31m" + m[0] + "\u001B[0m:" + m[1];
            }).collect(Collectors.joining("\n"));

            System.out.print("\n");
            System.out.println("Delta schema validation failed with errors:");
            System.out.println(errorMessage);
            System.out.print("\n");

            return 1;
        } else {
            System.out.println("Delta valid! Your changes have been saved.");

            return 0;
        }
    }
}
