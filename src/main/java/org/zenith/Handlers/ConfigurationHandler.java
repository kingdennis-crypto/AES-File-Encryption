package org.zenith.Handlers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

public class ConfigurationHandler {
    private static ConfigurationHandler instance;
    private final Properties properties;
    private final String filePath;

    private ConfigurationHandler() {
        this("config.properties");
    }

    private ConfigurationHandler(String filePath) {
        this.filePath = filePath;
        this.properties = new Properties();

        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                Files.createFile(path);
            }

            try (InputStream inputStream = new FileInputStream(filePath)) {
                properties.load(inputStream);
            }
        } catch (IOException ex) {
            System.err.printf("Error loading properties file: %s%n%n", ex.getMessage());
        }
    }

    public static ConfigurationHandler getInstance() {
        if (ConfigurationHandler.instance == null) {
            ConfigurationHandler.instance = new ConfigurationHandler();
        }

        if (instance.getProperty("KEY_PATH") == null) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Please give the path to where the keys should be stored: ");
                String keyPath = scanner.nextLine();

                instance.setProperty("KEY_PATH", keyPath);
                instance.saveProperties();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return ConfigurationHandler.instance;
    }

    private static void checkInitialisationFields() {
        Scanner scanner = new Scanner(System.in);

        if (instance.getProperty("KEY_PATH") == null) {
            System.out.print("Please give the path to where the keys should be stored: ");
            String keyPath = scanner.nextLine();

            instance.setProperty("KEY_PATH", keyPath);
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
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(filePath))) {
            properties.store(writer, "Updated properties");
        } catch (IOException ex) {
            System.err.println("Error saving properties file: " + ex.getMessage());
        }
    }
}
