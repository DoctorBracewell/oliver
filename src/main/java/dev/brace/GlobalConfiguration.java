package dev.brace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public class GlobalConfiguration {
    private Properties properties;
    private File configFile;

    GlobalConfiguration(File configFile) {
        this.properties = new Properties();
        this.configFile = configFile;

        try {
            new File(configFile.getParent()).mkdirs();
            configFile.createNewFile();
            loadConfig();
        } catch (IOException ex) {

        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);

        saveConfig();
    }

    private void loadConfig() {
        try {
            FileInputStream input = new FileInputStream(configFile);

            properties.load(input);
        } catch (IOException ex) {

        }
    }

    private void saveConfig() {
        try {
            FileOutputStream output = new FileOutputStream(configFile);

            properties.store(output, null);
        } catch (IOException ex) {

        }
    }

    public Path getConfigFilePath() {
        return configFile.toPath();
    }
}
