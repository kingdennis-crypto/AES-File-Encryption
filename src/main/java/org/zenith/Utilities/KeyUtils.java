package org.zenith.Utilities;

import org.zenith.Models.KeyProperties;
import org.zenith.Properties;

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

    private byte[] getRandomNonce() {
        byte[] nonce = new byte[12];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }

    public Path createKey(String keyName) throws IOException, NoSuchAlgorithmException {
        Path dirPath = Paths.get(path);

        if (!Files.exists(dirPath)) {
            Files.createDirectory(dirPath);
        }

        Path keyPath = Paths.get(String.format("%s/%s.key", path, keyName));

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

    public boolean deleteKey(String keyName) {
        try {
            boolean hasExtension = keyName.endsWith(".key");
            String format = hasExtension ? "%s/%s" : "%s/%s.key";

            // TODO: add check if key does not exist
            Path keyPath = Paths.get(String.format(format, path, keyName));

            return Files.deleteIfExists(keyPath);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

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
       } catch (IOException ex) {
           throw ex;
       }
    }
}
