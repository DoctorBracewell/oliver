package dev.brace.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

public class RepositoryDelta {
    private Properties properties;
    private File file;
    private Repository repository;
    private String id;

    public RepositoryDelta(File file, Repository repository, String id) {
        this.file = file;
        this.repository = repository;
        this.id = id;
    }

    public RepositoryDelta(File file, Repository repository) {
        this.file = file;
        this.repository = repository;
        this.id = "1";
    }

    public Set<ValidationMessage> validateFile() throws IOException {
        JsonSchema schema = this.repository.getDeltaSchema();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode deltaAsNode = mapper.readTree(file);

        return schema.validate(deltaAsNode);
    }

}
