package org.zenith.Handlers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationHandler {
    private final Properties properties;
    private final String filePath;

    public ConfigurationHandler() {
        this(ConfigurationHandler.class.getClassLoader().getResource("config.properties").getPath());
    }

    public ConfigurationHandler(String filePath) {
        // The path to the .properties file
        this.filePath = filePath;
        this.properties = new Properties();

        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        } catch (IOException ex) {
            System.err.printf("Error loading properties file: %s%n%n", ex.getMessage());
        }
    }

    /**
     * Get the value of a property specified by the key.
     * @param key The key of the property.
     * @return The value associated with the key, or null if the key is not found.
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get the value of a property specified by the key.
     * @param key The key of the property.
     * @param value The value to set for the property.
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    /**
     * Save the properties back to the .properties file.
     */
    public void saveProperties() {
        try (FileOutputStream output = new FileOutputStream(filePath)) {
            properties.store(output, "Updated properties");
        } catch (IOException ex) {
            System.err.println("Error saving properties file: " + ex.getMessage());
        }
    }
}
