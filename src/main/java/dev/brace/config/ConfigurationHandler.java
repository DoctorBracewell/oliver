package dev.brace.config;

import picocli.CommandLine.Model.OptionSpec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

public class ConfigurationHandler {
    private Properties properties;
    private File configFile;

    public ConfigurationHandler(File configFile) throws IOException {
        this.properties = new Properties();
        this.configFile = configFile;

        new File(configFile.getParent()).mkdirs();
        configFile.createNewFile();
        loadConfig();
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) throws IOException {
        properties.setProperty(key, value);

        saveConfig();
    }

    private void loadConfig() throws IOException {
        FileInputStream input = new FileInputStream(configFile);

        properties.load(input);
    }

    private void saveConfig() throws IOException {
        FileOutputStream output = new FileOutputStream(configFile);

        properties.store(output, null);
    }

    public Path getConfigFilePath() {
        return configFile.toPath();
    }

    public boolean setFromOptionList(List<OptionSpec> options) throws IOException {
        boolean anyConfigUpdated = false;

        for (OptionSpec option : options) {
            if (option.getValue() != null) {
                String onlyName = option.longestName().replace("--", "");

                this.setProperty(onlyName, option.getValue());
                anyConfigUpdated = true;
            }
        }

        return anyConfigUpdated;
    }
}
