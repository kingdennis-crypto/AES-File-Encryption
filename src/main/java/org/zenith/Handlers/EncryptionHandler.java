package org.zenith.Handlers;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.Base64;

public class EncryptionHandler {
    /**
     * Encrypts the given byte array using the AES/GCM/NoPadding algorithm.
     *
     * @param plain  The byte array to be encrypted.
     * @param secret The secret key used for the encryption.
     * @param iv     The initialization vector for encryption.
     * @return The encrypted byte array.
     * @throws InvalidParameterException If any of the input parameters is null.
     */
    public String encrypt(byte[] plain, SecretKey secret, byte[] iv) throws InvalidParameterException {
        try {
            if (plain == null || secret == null || iv == null)
                throw new InvalidParameterException();

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secret, new GCMParameterSpec(128, iv));
            byte[] cipherText = cipher.doFinal(plain);

            return Base64.getEncoder().encodeToString(cipherText);
        } catch (InvalidParameterException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Reads data from a file, processes using the specified {@link Cipher}, and writes the result back to the file.
     *
     * @param cipher      The cipher used for encryption and decryption.
     * @param path        The path to the input file.
     * @param willEncrypt A boolean indicating whether the data will be encrypted or decrypted.
     */
    private void readAndWriteToFile(Cipher cipher, String path, boolean willEncrypt) {
        try {
            File inputFile = new File(willEncrypt ? path : path + ".encrypted");
            File outputFile = new File(willEncrypt ? path + ".encrypted" : path);

            FileInputStream inputStream = new FileInputStream(inputFile);
            FileOutputStream outputStream = new FileOutputStream(outputFile);

            byte[] buffer = new byte[64];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);

                if (output != null) {
                    outputStream.write(output);
                }
            }

            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                outputStream.write(outputBytes);
            }

            inputStream.close();
            outputStream.close();
        } catch (IllegalBlockSizeException | BadPaddingException | IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Encrypts a file using AES/GCM/NoPadding algorithm.
     *
     * @param path   The path to the file to be encrypted.
     * @param secret The secret key used for encryption.
     * @param iv     The initialization vector for encryption
     */
    public void encryptFile(String path, SecretKey secret, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secret, new GCMParameterSpec(128, iv));

            readAndWriteToFile(cipher, path, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Decrypts a file using the AES/GCM/NoPadding algorithm.
     *
     * @param path   The path to the file to be encrypted.
     * @param secret The secret key used for decryption.
     * @param iv     The initialization vector for decryption.
     */
    public void decryptFile(String path, SecretKey secret, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new GCMParameterSpec(128, iv));

            readAndWriteToFile(cipher, path, false);

            // Removes the encrypted file after decryption
            Files.delete(Path.of(path + ".encrypted"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Converts a byte array to its hexadecimal representation.
     *
     * @param bytes The byte array to be represented in hexadecimal.
     * @return The hexadecimal representation
     */
    public String getHexRepresentation(String bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes.getBytes()) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    /**
     * Decrypts the given byte array using the AES/GCM/NoPadding algorithm.
     *
     * @param encrypt The encrypted byte array to be decrypted.
     * @param secret  The secret key used for the encryption.
     * @param iv      The initialization vector for encryption.
     * @return The decrypted string.
     * @throws InvalidParameterException If any of the input parameters is null.
     */
    public String decrypt(String encrypt, SecretKey secret, byte[] iv) {
        try {
            if (encrypt == null || secret == null || iv == null)
                throw new InvalidParameterException();

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new GCMParameterSpec(128, iv));

//            byte[] decryptedBytes = cipher.doFinal(encrypt);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encrypt));
            String decrypted = new String(decryptedBytes, StandardCharsets.UTF_8);

            return decrypted;
        } catch (InvalidParameterException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}