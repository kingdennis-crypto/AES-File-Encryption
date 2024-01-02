package org.zenith.Handlers;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.zenith.Handlers.EncryptionHandler;
import org.zenith.Handlers.KeyHandler;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionHandlerTest {
    @TempDir
    private Path tempDir;
    private KeyHandler keyHandler;
    private EncryptionHandler encryptionHandler;

    private String keyName;
    private String plain;

    @BeforeEach
    void init() throws IOException, NoSuchAlgorithmException {
        keyHandler = new KeyHandler(tempDir.toString());
        encryptionHandler = new EncryptionHandler();

        keyName = "key1";
        plain = "plain-text";

        keyHandler.createKey(keyName);
    }

    @AfterEach
    void cleanUp() throws IOException {
        Files.walk(tempDir)
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
    }

    @Test
    void testGetHexRepresentation() {
        assertEquals(encryptionHandler.getHexRepresentation(plain), "706c61696e2d74657874");
    }
}