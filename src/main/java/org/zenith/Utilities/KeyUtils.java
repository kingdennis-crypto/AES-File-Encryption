package org.zenith.Utilities;

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
    private String getPath() {
        return Properties.getProperty("keyFolderPath");
    }

    public void createKey(String keyName) {
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

            String secret = new String(keyGen.generateKey().getEncoded(), StandardCharsets.UTF_8);

            // TODO: Run test to see if you need to write as bytes not as string
            Files.writeString(keyPath, secret);
        } catch (IOException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
    }

    public SecretKey getKey(String keyName) {
        try {
            Path keyPath = Paths.get(String.format("%s/%s.key", getPath(), keyName));

            if (!Files.exists(keyPath)) {
                throw new FileNotFoundException(keyPath.toString());
            }

            return new SecretKeySpec(Files.readAllBytes(keyPath), "AES");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void deleteKey(String keyName) {
        try {
            Path keyPath = Paths.get(String.format("%s/%s.key", getPath(), keyName));

            Files.deleteIfExists(keyPath);
        } catch (IOException ex) {
            ex.printStackTrace();
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
