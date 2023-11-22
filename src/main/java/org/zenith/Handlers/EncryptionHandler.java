package org.zenith.Handlers;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;

public class EncryptionHandler {
    /**
     * Encrypts the given byte array using the AES/GCM/NoPadding algorithm.
     * @param plain The byte array to be encrypted.
     * @param secret The secret key used for the encryption.
     * @param iv The initialization vector for encryption.
     * @return The encrypted byte array.
     * @throws InvalidParameterException If any of the input parameters is null.
     */
    public byte[] encrypt(byte[] plain, SecretKey secret, byte[] iv) throws InvalidParameterException {
        try {
            if (plain == null || secret == null || iv == null)
                throw new InvalidParameterException();

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secret, new GCMParameterSpec(128, iv));

            return cipher.doFinal(plain);
        } catch(InvalidParameterException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Converts a byte array to its hexadecimal representation.
     * @param bytes The byte array to be represented in hexadecimal.
     * @return The hexadecimal representation
     */
    public String getHexRepresentation(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
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
    public String decrypt(byte[] encrypt, SecretKey secret, byte[] iv) {
        try {
            if (encrypt == null || secret == null || iv == null)
                throw new InvalidParameterException();

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new GCMParameterSpec(128, iv));

            return new String(cipher.doFinal(encrypt), StandardCharsets.UTF_8);
        } catch(InvalidParameterException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
