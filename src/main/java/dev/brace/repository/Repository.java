package dev.brace.repository;

import dev.brace.config.ConfigurationHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Repository {
    private Properties config = new Properties();

    Repository(Path path) throws IOException {
        File configFile = Paths.get(path.toString(), ".oliver", "config.properties").toFile();
        FileInputStream input = new FileInputStream(configFile);

        config.load(input);
    }

    static public void init(File configFile, String name) throws IOException {
        ConfigurationHandler configHandler = new ConfigurationHandler(configFile);

        configHandler.setProperty("name", name);

        System.out.printf("Initialised new repository \u001B[36m%s\u001B[0m.");
    }

    static public boolean inRepository(Path path) {
        try {
            new Repository(path);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
