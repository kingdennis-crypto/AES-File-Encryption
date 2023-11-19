package org.zenith.Utilities;

import org.zenith.Models.KeyProperties;
import org.zenith.Properties;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class KeyUtils {
    // TODO: Add constructor with interface to pass path as a parameter. This will also help with testing

    private String getPath() {
        return Properties.getProperty("keyFolderPath");
    }

    private byte[] getRandomNonce() {
        byte[] nonce = new byte[12];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }

    public Path createKey(String keyName) {
        try {
            Path dirPath = Paths.get(getPath());

            if (!Files.exists(dirPath)) {
                Files.createDirectory(dirPath);
            }

            // TODO: maybe change this to other file extension
            Path keyPath = Paths.get(String.format("%s/%s.key", getPath(), keyName));

            if (Files.exists(keyPath)) {
                throw new FileAlreadyExistsException(keyPath.toString());
            }

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

            // TODO: Change this to two separate files. One with .key and one with .iv
            //  This to make it easier to retrieve the data
            return Files.write(keyPath, content);
        } catch (IOException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public KeyProperties getKey(String keyName) {
        try {
            Path keyPath = Paths.get(String.format("%s/%s.key", getPath(), keyName));

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

//            System.out.println(Arrays.toString(nonce));
//            System.out.println(Arrays.toString(secret));

            return new KeyProperties(secret, nonce);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean deleteKey(String keyName) {
        try {
            Path keyPath = Paths.get(String.format("%s/%s.key", getPath(), keyName));

            return Files.deleteIfExists(keyPath);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<String> listKeys() {
       List<String> files = new ArrayList<>();
       Path dirPath = Paths.get(getPath());

       if (!Files.exists(dirPath)) {
           return files;
       }

       try (Stream<Path> filesInDir = Files.list(dirPath)) {
           for (Path filePath : filesInDir.toList()) {
               File file = new File(filePath.toUri());

               if (file.isFile()) {
                   files.add(file.getName());
               }
           }

           return files;
       } catch (IOException ex) {
           ex.printStackTrace();
           return new ArrayList<>();
       }
    }
}
