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
    void testEncryptAndDecrypt() {
        // Arrange
        SecretKey secretKey = new SecretKeySpec("0123456789012345".getBytes(), "AES");
        byte[] iv = "1234567890123456".getBytes();

        String encrypted = encryptionHandler.encrypt(plain.getBytes(), secretKey, iv);
        String decrypted = encryptionHandler.decrypt(encrypted, secretKey, iv);

        assertEquals(plain, decrypted);
    }

    @Test
    void testEncryptWithNullParameters() {
        SecretKey secretKey = new SecretKeySpec("0123456789012345".getBytes(), "AES");
        byte[] iv = "1234567890123456".getBytes();

        assertThrows(InvalidParameterException.class, () -> encryptionHandler.encrypt(null, secretKey, iv));
        assertThrows(InvalidParameterException.class, () -> encryptionHandler.encrypt(plain.getBytes(), null, iv));
        assertThrows(InvalidParameterException.class, () -> encryptionHandler.encrypt(plain.getBytes(), secretKey, null));
    }

    @Test
    void testDecryptWithNullParameters() {
        SecretKey secretKey = new SecretKeySpec("0123456789012345".getBytes(), "AES");
        byte[] iv = "1234567890123456".getBytes();

        assertThrows(InvalidParameterException.class, () -> encryptionHandler.decrypt(null, secretKey, iv));
        assertThrows(InvalidParameterException.class, () -> encryptionHandler.decrypt(plain, null, iv));
        assertThrows(InvalidParameterException.class, () -> encryptionHandler.decrypt(plain, secretKey, null));
    }

    @Test
    void testGetHexRepresentation() {
        assertEquals(encryptionHandler.getHexRepresentation(plain), "706c61696e2d74657874");
    }
}