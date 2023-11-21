package org.zenith.Utilities;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

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

class KeyUtilsTest {
    @TempDir
    private Path tempDir;
    private KeyUtils keyUtils;
    private List<String> keys;

    @BeforeEach
    void init() {
        keyUtils = new KeyUtils(tempDir.toString());
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
        Path key1 = keyUtils.createKey(keys.get(0));

        FileAlreadyExistsException exception = assertThrows(
                FileAlreadyExistsException.class, () -> keyUtils.createKey(keys.get(0)));

        assertEquals(exception.getMessage(), key1.toString());
    }

    @Test
    void shouldReturnCorrectNumberOfKeys() throws IOException {
        keys.forEach(key -> assertDoesNotThrow(() -> keyUtils.createKey(key)));
        assertEquals(keys.size(), keyUtils.listKeys().size());
        keys.forEach(key -> assertDoesNotThrow(() -> keyUtils.deleteKey(key)));

        assertNotEquals(keys.size(), keyUtils.listKeys().size());
        assertEquals(0, keyUtils.listKeys().size());
    }

    @Test
    void shouldDeleteKeyWithOrWithoutExtension() throws IOException {
        // Create keys
        keys.forEach(key -> assertDoesNotThrow(() -> keyUtils.createKey(key)));
        assertEquals(keys.size(), keyUtils.listKeys().size());

        // Delete keys
        keys.forEach(key -> assertDoesNotThrow(() -> keyUtils.deleteKey(key + ".key")));
        assertEquals(keyUtils.listKeys().size(), 0);

        // Create keys
        keys.forEach(key -> assertDoesNotThrow(() -> keyUtils.createKey(key)));
        assertEquals(keys.size(), keyUtils.listKeys().size());

        // Delete keys
        keys.forEach(key -> assertDoesNotThrow(() -> keyUtils.deleteKey(key)));
        assertEquals(keyUtils.listKeys().size(), 0);
    }

    @Test
    void shouldThrowFileNotFoundExceptionWhenKeyDoesNotExist() {
        String nonExistingKey = "non-existing";

        FileNotFoundException exception = assertThrows(
                FileNotFoundException.class, () -> keyUtils.getKey(nonExistingKey));

        assertEquals(exception.getMessage(), tempDir.resolve(nonExistingKey + ".key").toString());
    }
}