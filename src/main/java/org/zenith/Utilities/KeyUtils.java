package org.zenith.Utilities;

import org.zenith.Models.KeyProperties;

import javax.crypto.KeyGenerator;
import java.io.*;
import java.nio.file.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class KeyUtils {
    private final String path;

    public KeyUtils(String path) {
        this.path = path;
    }

    /**
     * Generates a random nonce for the IV part of the key.
     * @return The generated nonce.
     */
    private byte[] getRandomNonce() {
        byte[] nonce = new byte[12];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }

    /**
     * Checks if the provided key includes '.key' and returns the correct format for the key name.
     * @param keyName The provided key name with or without '.key'.
     * @return The correct format for the path and key name.
     */
    private String getKeyFormat(String keyName) {
        return keyName.endsWith(".key") ? "%s/%s" : "%s/%s.key";
    }

    /**
     * Creates a new key with the specified name.
     * @param keyName The name of the key to be created.
     * @return The path to the newly created key file.
     * @throws IOException If an I/O error occurs.
     * @throws NoSuchAlgorithmException If the specified cryptographic algorithm is not available.
     */
    public Path createKey(String keyName) throws IOException, NoSuchAlgorithmException {
        Path dirPath = Paths.get(path);

        if (!Files.exists(dirPath))
            Files.createDirectory(dirPath);

        String keyFormat = getKeyFormat(keyName);

        Path keyPath = Paths.get(String.format(keyFormat, path, keyName));

        if (Files.exists(keyPath))
            throw new FileAlreadyExistsException(keyPath.toString());

        Files.createFile(keyPath);

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        // Size 128, 192 or 256
        keyGen.init(256, SecureRandom.getInstanceStrong());

        byte[] secret = keyGen.generateKey().getEncoded();
        byte[] nonce = getRandomNonce();

        byte[] content = new byte[nonce.length + secret.length];

        for (int i = 0; i < content.length; i++) {
            content[i] = i < nonce.length ? nonce[i] : secret[i - nonce.length];
        }

        return Files.write(keyPath, content);
    }

    /**
     * Retrieves the {@link KeyProperties} for the specified key name.
     * @param keyName The name of the key.
     * @return The {@link KeyProperties} containing the key's secret and nonce.
     * @throws IOException If an I/O error occurs.
     */
    public KeyProperties getKey(String keyName) throws IOException {
        Path keyPath = Paths.get(String.format("%s/%s.key", path, keyName));

        if (!Files.exists(keyPath)) {
            throw new FileNotFoundException(keyPath.toString());
        }

        byte[] keyContent = Files.readAllBytes(keyPath);

        byte[] nonce = new byte[12];
        byte[] secret = new byte[keyContent.length - 12];

        for (int i = 0; i < keyContent.length; i++) {
            if (i < 12) {
                nonce[i] = keyContent[i];
            } else {
                secret[i - nonce.length] = keyContent[i];
            }
        }

        return new KeyProperties(secret, nonce);
    }

    /**
     * Deletes the key with the specified name.
     * @param keyName The name of the key to be deleted.
     * @throws IOException If an I/O exception occurs.
     */
    public void deleteKey(String keyName) throws IOException {
        String keyFormat = getKeyFormat(keyName);
        Path keyPath = Paths.get(String.format(keyFormat, path, keyName));

        if (!Files.exists(keyPath))
            throw new FileNotFoundException(keyPath.toString());

        Files.delete(keyPath);
    }

    /**
     * Lists all key files in the key directory.
     * @return A list of key file names.
     * @throws IOException If an I/O error occurs.
     */
    public List<String> listKeys() throws IOException {
       List<String> files = new ArrayList<>();
       Path dirPath = Paths.get(path);

       if (!Files.exists(dirPath)) {
           return files;
       }

       try (Stream<Path> filesInDir = Files.list(dirPath)) {
           for (Path filePath : filesInDir.toList()) {
               File file = new File(filePath.toUri());

               if (file.isFile() && file.getName().endsWith(".key")) {
                   files.add(file.getName());
               }
           }

           return files;
       }
    }
}
