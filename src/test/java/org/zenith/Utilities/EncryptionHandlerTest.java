package org.zenith.Utilities;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.zenith.Models.KeyProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionHandlerTest {
    @TempDir
    private Path tempDir;
    private KeyUtils keyUtils;
    private EncryptionHandler encryptionHandler;

    private String keyName;
    private String plain;

    @BeforeEach
    void init() throws IOException, NoSuchAlgorithmException {
        keyUtils = new KeyUtils(tempDir.toString());
        encryptionHandler = new EncryptionHandler();

        keyName = "key1";
        plain = "plain-text";

        keyUtils.createKey(keyName);
    }

    @AfterEach
    void cleanUp() throws IOException {
        Files.walk(tempDir)
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
    }

    @Test
    void shouldEncryptWithCorrectValue() throws IOException {
        KeyProperties keyProperties = keyUtils.getKey(keyName);
        byte[] encrypted = assertDoesNotThrow(() ->
                encryptionHandler.encrypt(plain.getBytes(), keyProperties.getSecretKey(), keyProperties.getNonce()));

        assertNotNull(encrypted);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWithInvalidParameters() throws IOException {
        KeyProperties keyProperties = keyUtils.getKey(keyName);

        assertThrows(InvalidParameterException.class, () -> encryptionHandler.encrypt(plain.getBytes(), null, null));
        assertThrows(InvalidParameterException.class, () -> encryptionHandler.encrypt(plain.getBytes(), null, keyProperties.getNonce()));
        assertThrows(InvalidParameterException.class, () -> encryptionHandler.encrypt(plain.getBytes(), keyProperties.getSecretKey(), null));
        assertThrows(InvalidParameterException.class, () -> encryptionHandler.encrypt(null, keyProperties.getSecretKey(), keyProperties.getNonce()));
    }
}