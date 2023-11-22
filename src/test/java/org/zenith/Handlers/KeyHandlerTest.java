package org.zenith.Handlers;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.zenith.Models.KeyProperties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KeyHandlerTest {
    @TempDir
    private Path tempDir;
    private KeyHandler keyHandler;
    private List<String> keys;

    @BeforeEach
    void init() {
        keyHandler = new KeyHandler(tempDir.toString());
        keys = List.of("key1", "key2", "key3");
    }

    @AfterEach
    void cleanUp() throws IOException {
        Files.walk(tempDir)
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
    }

    @Test
    void shouldThrowExceptionForDuplicateKey() throws IOException, NoSuchAlgorithmException {
        Path key1 = keyHandler.createKey(keys.get(0));

        FileAlreadyExistsException exception = assertThrows(
                FileAlreadyExistsException.class, () -> keyHandler.createKey(keys.get(0)));

        assertEquals(exception.getMessage(), key1.toString());
    }

    @Test
    void shouldReturnCorrectNumberOfKeys() throws IOException {
        keys.forEach(key -> assertDoesNotThrow(() -> keyHandler.createKey(key)));
        assertEquals(keys.size(), keyHandler.listKeys().size());
        keys.forEach(key -> assertDoesNotThrow(() -> keyHandler.deleteKey(key)));

        assertNotEquals(keys.size(), keyHandler.listKeys().size());
        assertEquals(0, keyHandler.listKeys().size());
    }

    @Test
    void shouldDeleteKeyWithOrWithoutExtension() throws IOException {
        // Create keys
        keys.forEach(key -> assertDoesNotThrow(() -> keyHandler.createKey(key)));
        assertEquals(keys.size(), keyHandler.listKeys().size());

        // Delete keys
        keys.forEach(key -> assertDoesNotThrow(() -> keyHandler.deleteKey(key + ".key")));
        assertEquals(keyHandler.listKeys().size(), 0);

        // Create keys
        keys.forEach(key -> assertDoesNotThrow(() -> keyHandler.createKey(key)));
        assertEquals(keys.size(), keyHandler.listKeys().size());

        // Delete keys
        keys.forEach(key -> assertDoesNotThrow(() -> keyHandler.deleteKey(key)));
        assertEquals(keyHandler.listKeys().size(), 0);
    }

    @Test
    void shouldThrowFileNotFoundExceptionWhenKeyDoesNotExist() {
        String nonExistingKey = "non-existing";

        FileNotFoundException exception = assertThrows(
                FileNotFoundException.class, () -> keyHandler.getKey(nonExistingKey));

        assertEquals(exception.getMessage(), tempDir.resolve(nonExistingKey + ".key").toString());
    }

    @Test
    void shouldDeleteExistingKeyWithoutException() throws IOException, NoSuchAlgorithmException {
        Path key = keyHandler.createKey(keys.get(0));

        assertDoesNotThrow(() -> keyHandler.deleteKey(key.getFileName().toString()));
    }

    @Test
    void shouldThrowFileNotFoundWhenDeletingKeyThatDoesNotExist() {
        String nonExistingKey = "non-existing";

        FileNotFoundException exception = assertThrows(
                FileNotFoundException.class, () -> keyHandler.deleteKey(nonExistingKey));

        assertEquals(exception.getMessage(), tempDir.resolve(nonExistingKey + ".key").toString());
    }

    @Test
    void shouldHaveCorrectLengthOfKeyContents() throws IOException, NoSuchAlgorithmException {
        keyHandler.createKey(keys.get(0));

        KeyProperties properties = keyHandler.getKey(keys.get(0));

        assertNotNull(properties);
        assertEquals(properties.getNonce().length, 12);
    }
}