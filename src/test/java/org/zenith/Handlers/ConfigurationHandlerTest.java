package org.zenith.Handlers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationHandlerTest {
    private ConfigurationHandler configurationHandler;

    @BeforeEach
    void setup() {
        // Set up the configuration handler before each test
        configurationHandler = ConfigurationHandler.getInstance();
    }

    @AfterEach
    void tearDown() {
        // Clean up any files created during the test
        Path filePath = Paths.get("config.properties");

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void getInstanceShouldReturnSameSingleton() {
        ConfigurationHandler instance1 = ConfigurationHandler.getInstance();
        ConfigurationHandler instance2 = ConfigurationHandler.getInstance();

        // assertSame checks if they are the same object, and not just the same content
        assertSame(instance1, instance2);
    }

    @Test
    void shouldReturnValidKeyIfExist() {
        configurationHandler.setProperty("TEST_KEY", "foo");

        String keyValue = assertDoesNotThrow(() -> configurationHandler.getProperty("TEST_KEY"));

        assertEquals(keyValue, "foo", "Value should match the given value");
    }

    @Test
    void shouldReturnNullWithInvalidKey() {
        String keyValue = assertDoesNotThrow(() -> configurationHandler.getProperty("NON_EXISTING"));
        assertNull(keyValue, "Value should be null for a non-existing key");
    }
}