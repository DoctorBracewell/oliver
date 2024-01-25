package dev.brace.repository;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import dev.brace.config.ConfigurationHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Repository {
    private ConfigurationHandler configHandler;
    private JsonSchema deltaSchema;

    private Path configFilePath;
    private Path deltaSchemaFilePath;

    public Repository(Path path) throws IOException {
        this.configFilePath = Paths.get(path.toString(), ".oliver", "config.properties");
        this.deltaSchemaFilePath = Paths.get(path.toString(), ".oliver", "delta-schema.json");

        this.configHandler = new ConfigurationHandler(configFilePath.toFile());

        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        this.deltaSchema = factory.getSchema(new String(Files.readAllBytes(this.deltaSchemaFilePath)));
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


    public JsonSchema getDeltaSchema() {
        return deltaSchema;
    }
}
