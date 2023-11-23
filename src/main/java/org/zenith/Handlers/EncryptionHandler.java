package org.zenith.Handlers;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.Base64;

public class EncryptionHandler {
    /**
     * Encrypts the given byte array using the AES/GCM/NoPadding algorithm.
     * @param plain The byte array to be encrypted.
     * @param secret The secret key used for the encryption.
     * @param iv The initialization vector for encryption.
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
        } catch(InvalidParameterException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void encryptFile(String path, SecretKey secret, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secret, new GCMParameterSpec(128, iv));

            File inputFile = new File(path);
            File outputFile = new File(path + ".encrypted");

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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void decryptFile(String path, SecretKey secret, byte[] iv) { {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new GCMParameterSpec(128, iv));

            File inputFile = new File(path + ".encrypted");
            File outputFile = new File(path);

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

            byte[] hello = "Hello again!".getBytes();
            byte[] outputBytes = cipher.doFinal();

            byte[] output = new byte[hello.length + outputBytes.length];

            for (int i = 0; i < output.length; i++) {
                output[i] = i < outputBytes.length ? outputBytes[i] : hello[i - outputBytes.length];
            }


            if (outputBytes != null) {
                outputStream.write(output);
            }

            inputStream.close();
            outputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    }

    /**
     * Converts a byte array to its hexadecimal representation.
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
     * @param encrypt The encrypted byte array to be decrypted.
     * @param secret The secret key used for the encryption.
     * @param iv The initialization vector for encryption.
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
            String decrypted =  new String(decryptedBytes, StandardCharsets.UTF_8);

            return decrypted;
        } catch(InvalidParameterException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
